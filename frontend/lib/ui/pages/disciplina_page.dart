import 'package:flutter/material.dart';
import 'package:frontend/models/aluno.dart';
import '../../datasources/remote/disciplina_remote.dart';
import '../../models/disciplina.dart';

class DisciplinasPage extends StatefulWidget {
  const DisciplinasPage({super.key, required int turmaId, required Aluno aluno});

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
      _filteredDisciplinas =
          _disciplina
              .where(
                (disciplina) =>
                disciplina.nome.toLowerCase().contains(query.toLowerCase()),
          )
              .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Disciplinas')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: SizedBox(
              child: TextField(
                decoration: const InputDecoration(
                  labelText: 'Buscar por nome',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.search),
                ),
                onChanged: _filterDisciplinas,
              ),
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
                  return const Center(child: Text('Nenhuma disciplina encontrada.'));
                }

                return ListView.builder(
                  itemCount: _filteredDisciplinas.length,
                  itemBuilder: (context, index) {
                    final disciplina = _filteredDisciplinas[index];
                    return ListTile(
                      title: Text(disciplina.nome),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {

                      },
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
