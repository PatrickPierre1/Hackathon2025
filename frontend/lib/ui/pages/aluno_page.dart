import 'package:flutter/material.dart';
import '../../models/turma.dart';
import '../../models/aluno.dart';
import 'disciplina_page.dart'; // ou próxima tela desejada

class AlunosPage extends StatefulWidget {
  final Turma turma;

  const AlunosPage({super.key, required this.turma});

  @override
  State<AlunosPage> createState() => _AlunosPageState();
}

class _AlunosPageState extends State<AlunosPage> {
  late List<Aluno> _alunos;
  List<Aluno> _filteredAlunos = [];
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _alunos = widget.turma.alunos;
    _filteredAlunos = _alunos;
  }

  void _filterAlunos(String query) {
    setState(() {
      _searchQuery = query;
      _filteredAlunos = _alunos.where((aluno) {
        final nomeMatch = aluno.nome.toLowerCase().contains(query.toLowerCase());
        final raMatch = aluno.ra.toLowerCase().contains(query.toLowerCase());
        return nomeMatch || raMatch;
      }).toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Alunos - ${widget.turma.nome}')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: SizedBox(
              child: TextField(
                decoration: const InputDecoration(
                  labelText: 'Buscar por nome ou RA',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.search),
                ),
                onChanged: _filterAlunos,
              ),
            ),
          ),
          Expanded(
            child: _filteredAlunos.isEmpty
                ? const Center(child: Text('Nenhum aluno encontrado.'))
                : ListView.builder(
              itemCount: _filteredAlunos.length,
              itemBuilder: (context, index) {
                final aluno = _filteredAlunos[index];
                return ListTile(
                  title: Text(aluno.nome),
                  subtitle: Text('RA: ${aluno.ra}'),
                  trailing: const Icon(Icons.arrow_forward_ios),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => DisciplinasPage(
                          turmaId: widget.turma.id,
                          aluno: aluno,
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
