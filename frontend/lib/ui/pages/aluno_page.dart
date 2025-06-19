import 'package:flutter/material.dart';
import '../../models/turma.dart';
import '../../models/aluno.dart';
import '../widgets/custom_appbar.dart';
import 'disciplina_page.dart';
import '../widgets/custom_appbar.dart';

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
      backgroundColor: Colors.grey[100],
      appBar: CustomAppBar(title: 'Alunos - ${widget.turma.nome}'),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Buscar por nome ou RA...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                filled: true,
                fillColor: Colors.white,
              ),
              onChanged: _filterAlunos,
            ),
          ),
          Expanded(
            child: _filteredAlunos.isEmpty
                ? const Center(
              child: Text(
                'Nenhum aluno encontrado.',
                style: TextStyle(fontSize: 16),
              ),
            )
                : ListView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              itemCount: _filteredAlunos.length,
              itemBuilder: (context, index) {
                final aluno = _filteredAlunos[index];
                return Card(
                  margin: const EdgeInsets.symmetric(vertical: 8),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                  ),
                  elevation: 2,
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(
                      horizontal: 20,
                      vertical: 12,
                    ),
                    leading: CircleAvatar(
                      backgroundColor: Colors.blue[100],
                      child: Text(
                        aluno.nome.substring(0, 1).toUpperCase(),
                        style: const TextStyle(color: Colors.blueAccent),
                      ),
                    ),
                    title: Text(
                      aluno.nome,
                      style: const TextStyle(
                        fontWeight: FontWeight.w600,
                        fontSize: 16,
                      ),
                    ),
                    subtitle: Text('RA: ${aluno.ra}'),
                    trailing: const Icon(Icons.arrow_forward_ios_rounded),
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
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
