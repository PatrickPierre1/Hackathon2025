import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/ui/widgets/custom_appbar.dart';
import 'package:google_mlkit_text_recognition/google_mlkit_text_recognition.dart';
import 'package:image_picker/image_picker.dart';
import '../../datasources/remote/gabarito_remote.dart';

class GabaritoOCRPage extends StatefulWidget {
  final int alunoId;
  const GabaritoOCRPage({super.key, required this.alunoId});

  @override
  State<GabaritoOCRPage> createState() => _GabaritoOCRPageState();
}

class _GabaritoOCRPageState extends State<GabaritoOCRPage> {
  Map<int, String> respostas = {};
  bool carregando = false;
  String? erro;

  Future<void> _lerImagem() async {
    setState(() {
      carregando = true;
      erro = null;
      respostas = {};
    });

    try {
      final picker = ImagePicker();
      final XFile? image = await picker.pickImage(source: ImageSource.camera);

      if (image == null) {
        setState(() {
          carregando = false;
          erro = 'Imagem não capturada.';
        });
        return;
      }

      final inputImage = InputImage.fromFilePath(image.path);
      final textRecognizer = TextRecognizer();
      final RecognizedText recognizedText =
      await textRecognizer.processImage(inputImage);

      final Map<int, String> detectadas = {};
      final RegExp padrao = RegExp(r'(\d{1,2})\s*[-–]\s*([A-Ea-e])');

      for (final block in recognizedText.blocks) {
        for (final line in block.lines) {
          final match = padrao.firstMatch(line.text);
          if (match != null) {
            final numero = int.tryParse(match.group(1)!);
            final letra = match.group(2)!.toUpperCase();
            if (numero != null) {
              detectadas[numero] = letra;
            }
          }
        }
      }

      setState(() {
        respostas = detectadas;
        carregando = false;
      });

      await textRecognizer.close();
    } catch (e) {
      setState(() {
        carregando = false;
        erro = 'Erro ao processar imagem: $e';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: 'Leitor de Gabarito'),
      backgroundColor: Colors.grey[100],
      floatingActionButton: respostas.isNotEmpty
          ? FloatingActionButton.extended(
        onPressed: _enviarRespostas,
        icon: const Icon(Icons.send),
        label: const Text('Enviar'),
        backgroundColor: Colors.blueAccent,
      )
          : null,
      body: Column(
        children: [
          const SizedBox(height: 32),
          Center(
            child: ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                foregroundColor: Colors.white,
                backgroundColor: Colors.blueAccent,
                padding:
                const EdgeInsets.symmetric(horizontal: 40, vertical: 20),
                textStyle: const TextStyle(fontSize: 18),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              onPressed: carregando ? null : _lerImagem,
              icon: const Icon(Icons.camera_alt, size: 28, color: Colors.white),
              label: const Text('Ler Gabarito com Câmera'),

            ),
          ),
          const SizedBox(height: 24),
          if (carregando) const CircularProgressIndicator(),
          if (erro != null)
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                erro!,
                style: const TextStyle(color: Colors.red),
              ),
            ),
          if (respostas.isNotEmpty)
            Expanded(
              child: ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: respostas.length,
                itemBuilder: (context, index) {
                  final questao = respostas.keys.elementAt(index);
                  final resposta = respostas[questao]!;
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 6),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    elevation: 2,
                    child: ListTile(
                      title: Text(
                        'Questão $questao',
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                      trailing: Text(
                        resposta,
                        style: const TextStyle(
                          color: Colors.blueAccent,
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
        ],
      ),
    );
  }

  // Future<void> _enviarRespostas() async {
  //   final gabaritoRemote = GabaritoRemoteDataSource();
  //
  //   try {
  //     await gabaritoRemote.enviarRespostas(
  //       alunoId: widget.alunoId,
  //       provaId: '456',
  //       respostas: respostas,
  //     );
  //
  //     ScaffoldMessenger.of(context).showSnackBar(
  //       const SnackBar(content: Text('Gabarito enviado com sucesso!')),
  //     );
  //   } catch (e) {
  //     ScaffoldMessenger.of(context).showSnackBar(
  //       SnackBar(content: Text('Erro ao enviar: $e')),
  //     );
  //   }
  // }

  void _enviarRespostas() {
    final alunoId = widget.alunoId;
    final provaId = '456';

    final jsonSimulado = {
      'alunoId': alunoId,
      'provaId': provaId,
      'respostas': respostas.map((k, v) => MapEntry(k.toString(), v)),
    };

    final jsonFormatado = const JsonEncoder.withIndent('  ').convert(jsonSimulado);

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('JSON que será enviado'),
        content: SingleChildScrollView(child: Text(jsonFormatado)),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Fechar'),
          ),
        ],
        scrollable: true,
      ),
    );
  }
}

