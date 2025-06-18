import 'package:flutter/material.dart';
import 'package:frontend/ui/pages/aluno_page.dart';
import '../../datasources/remote/turma_remote.dart';
import '../../models/turma.dart';

class TurmasPage extends StatefulWidget {
  const TurmasPage({super.key});

  @override
  State<TurmasPage> createState() => _TurmasPageState();
}

class _TurmasPageState extends State<TurmasPage> {
  late Future<List<Turma>> _turmasFuture;
  List<Turma> _turmas = [];
  List<Turma> _filteredTurmas = [];
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _turmasFuture = TurmaRemoteDataSource().getTurmas();
    _turmasFuture.then((turmas) {
      setState(() {
        _turmas = turmas;
        _filteredTurmas = turmas;
      });
    });
  }

  void _filterTurmas(String query) {
    setState(() {
      _searchQuery = query;
      _filteredTurmas =
          _turmas
              .where(
                (turma) =>
                    turma.nome.toLowerCase().contains(query.toLowerCase()),
              )
              .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Turmas')),
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
                onChanged: _filterTurmas,
              ),
            ),
          ),
          Expanded(
            child: FutureBuilder<List<Turma>>(
              future: _turmasFuture,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                }

                if (snapshot.hasError) {
                  return Center(child: Text('Erro: ${snapshot.error}'));
                }

                if (_filteredTurmas.isEmpty) {
                  return const Center(child: Text('Nenhuma turma encontrada.'));
                }

                return ListView.builder(
                  itemCount: _filteredTurmas.length,
                  itemBuilder: (context, index) {
                    final turma = _filteredTurmas[index];
                    return ListTile(
                      leading: Icon(Icons.groups, color: Colors.blueAccent),
                      title: Text(turma.nome),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => AlunosPage(turma: turma,),
                          ),
                        );
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
