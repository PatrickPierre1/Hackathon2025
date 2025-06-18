import 'dart:convert';

import 'package:flutter/material.dart';
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
      appBar: AppBar(title: const Text('Leitor de Gabarito')),
      floatingActionButton: respostas.isNotEmpty
          ? FloatingActionButton.extended(
        onPressed: _enviarRespostas,
        icon: const Icon(Icons.send),
        label: const Text('Enviar'),
      )
          : null,
      body: Column(
        children: [
          const SizedBox(height: 16),
          ElevatedButton.icon(
            onPressed: carregando ? null : _lerImagem,
            icon: const Icon(Icons.camera_alt),
            label: const Text('Ler Gabarito com Câmera'),
          ),
          const SizedBox(height: 16),
          if (carregando) const CircularProgressIndicator(),
          if (erro != null)
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(erro!, style: const TextStyle(color: Colors.red)),
            ),
          if (respostas.isNotEmpty)
            Expanded(
              child: ListView(
                children: respostas.entries
                    .map(
                      (entry) =>
                      ListTile(
                        title: Text('Questão ${entry.key}'),
                        trailing: Text(entry.value),
                      ),
                )
                    .toList(),
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

