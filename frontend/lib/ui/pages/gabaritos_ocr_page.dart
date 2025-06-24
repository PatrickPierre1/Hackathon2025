import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:google_mlkit_text_recognition/google_mlkit_text_recognition.dart';
import 'package:image_picker/image_picker.dart';
import '../../ui/widgets/custom_appbar.dart';

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

  String? _disciplinaSelecionada;
  String? _provaSelecionada;

  final List<String> disciplinas = ['Matemática', 'Português', 'Física'];
  final List<String> provas = ['Prova 1', 'Prova 2', 'Prova Final'];

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
      final recognizedText = await textRecognizer.processImage(inputImage);

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
  void initState() {
    super.initState();

    for (int i = 1; i <= 10; i++) {
      respostas[i] = '';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: 'Leitor de Gabarito'),
      backgroundColor: Colors.grey[100],
      body: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                _buildDropdowns(),
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
                      itemCount: 10,
                      itemBuilder: (context, index) {
                        final questao = index + 1;
                        final marcada = respostas[questao] ?? '';
                        return _buildGabaritoVisual(questao, marcada);
                      },
                    ),
                  ),
                SizedBox(
                  height: 55,
                ),
              ],
            ),
          ),
          Positioned(
            left: 16,
            bottom: 16,
            child: FloatingActionButton.extended(
              onPressed: carregando ? null : _lerImagem,
              icon: const Icon(Icons.camera_alt),
              label: const Text('Ler Gabarito'),
              backgroundColor: Colors.blueAccent,
              foregroundColor: Colors.white,
            ),
          ),
          if (respostas.isNotEmpty)
            Positioned(
              right: 16,
              bottom: 16,
              child: FloatingActionButton.extended(
                onPressed: _enviarRespostas,
                icon: const Icon(Icons.send),
                label: const Text('Enviar'),
                backgroundColor: Colors.blueAccent,
                foregroundColor: Colors.white,
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildDropdowns() {
    return Column(
      children: [
        DropdownButtonFormField<String>(
          value: _disciplinaSelecionada,
          decoration: const InputDecoration(
            labelText: 'Selecione a Disciplina',
            border: OutlineInputBorder(),
          ),
          items: disciplinas
              .map((d) => DropdownMenuItem(value: d, child: Text(d)))
              .toList(),
          onChanged: (value) => setState(() => _disciplinaSelecionada = value),
        ),
        const SizedBox(height: 12),
        DropdownButtonFormField<String>(
          value: _provaSelecionada,
          decoration: const InputDecoration(
            labelText: 'Selecione a Prova',
            border: OutlineInputBorder(),
          ),
          items: provas
              .map((p) => DropdownMenuItem(value: p, child: Text(p)))
              .toList(),
          onChanged: (value) => setState(() => _provaSelecionada = value),
        ),
      ],
    );
  }

  Widget _buildGabaritoVisual(int questao, String marcada) {
    final letras = ['A', 'B', 'C', 'D', 'E'];
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 6),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      elevation: 2,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Row(
          children: [
            Text('$questao', style: const TextStyle(fontSize: 16)),
            const SizedBox(width: 12),
            ...letras.map((letra) {
              final selecionada = letra == marcada;
              return Padding(
                padding: const EdgeInsets.symmetric(horizontal: 6),
                child: InkWell(
                  onTap: () {
                    setState(() {
                      respostas[questao] = letra;
                    });
                  },
                  child: CircleAvatar(
                    radius: 18,
                    backgroundColor:
                    selecionada ? Colors.blueAccent : Colors.grey[300],
                    child: Text(
                      letra,
                      style: TextStyle(
                        color: selecionada ? Colors.white : Colors.black,
                        fontWeight:
                        selecionada ? FontWeight.bold : FontWeight.normal,
                      ),
                    ),
                  ),
                ),
              );
            }).toList(),
          ],
        ),
      ),
    );
  }

  void _enviarRespostas() {
    final alunoId = widget.alunoId;
    final provaId = '456';

    final jsonSimulado = {
      'alunoId': alunoId,
      'provaId': provaId,
      'respostas': respostas.map((k, v) => MapEntry(k.toString(), v)),
    };

    final jsonFormatado =
    const JsonEncoder.withIndent('  ').convert(jsonSimulado);

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
