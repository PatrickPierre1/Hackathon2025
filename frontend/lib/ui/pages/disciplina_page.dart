import 'package:flutter/material.dart';
import 'package:frontend/models/aluno.dart';
import 'package:frontend/ui/pages/gabaritos_ocr_page.dart';
import 'package:frontend/ui/widgets/custom_appbar.dart';
import '../../datasources/remote/disciplina_remote.dart';
import '../../models/disciplina.dart';
import '../widgets/custom_appbar.dart';

class DisciplinasPage extends StatefulWidget {
  final int turmaId;
  final Aluno aluno;

  const DisciplinasPage({
    super.key,
    required this.turmaId,
    required this.aluno,
  });

  @override
  State<DisciplinasPage> createState() => _DisciplinasPageState();
}

class _DisciplinasPageState extends State<DisciplinasPage> {
  late Future<List<Disciplina>> _disciplinaFuture;
  List<Disciplina> _disciplina = [];
  List<Disciplina> _filteredDisciplinas = [];
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _disciplinaFuture = DisciplinaRemoteDataSource().getDisciplinas();
    _disciplinaFuture.then((disciplinas) {
      setState(() {
        _disciplina = disciplinas;
        _filteredDisciplinas = disciplinas;
      });
    });
  }

  void _filterDisciplinas(String query) {
    setState(() {
      _searchQuery = query;
      _filteredDisciplinas = _disciplina
          .where((disciplina) =>
          disciplina.nome.toLowerCase().contains(query.toLowerCase()))
          .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: const CustomAppBar(title: 'Disciplinas'),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Buscar por nome da disciplina...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                filled: true,
                fillColor: Colors.white,
              ),
              onChanged: _filterDisciplinas,
            ),
          ),
          Expanded(
            child: FutureBuilder<List<Disciplina>>(
              future: _disciplinaFuture,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                }

                if (snapshot.hasError) {
                  return Center(child: Text('Erro: ${snapshot.error}'));
                }

                if (_filteredDisciplinas.isEmpty) {
                  return const Center(
                    child: Text(
                      'Nenhuma disciplina encontrada.',
                      style: TextStyle(fontSize: 16),
                    ),
                  );
                }

                return ListView.builder(
                  padding: const EdgeInsets.symmetric(horizontal: 12),
                  itemCount: _filteredDisciplinas.length,
                  itemBuilder: (context, index) {
                    final disciplina = _filteredDisciplinas[index];
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
                          child: const Icon(Icons.menu_book, color: Colors.blueAccent),
                        ),
                        title: Text(
                          disciplina.nome,
                          style: const TextStyle(
                            fontWeight: FontWeight.w600,
                            fontSize: 16,
                          ),
                        ),
                        trailing: const Icon(Icons.arrow_forward_ios_rounded),
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => GabaritoOCRPage(
                                alunoId: widget.aluno.id,
                              ),
                            ),
                          );
                        },
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
