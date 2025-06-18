import 'package:flutter/material.dart';
import 'package:frontend/ui/pages/aluno_page.dart';
import 'package:frontend/ui/widgets/custom_appbar.dart';
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
      _filteredTurmas = _turmas
          .where(
            (turma) => turma.nome.toLowerCase().contains(query.toLowerCase()),
      )
          .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: CustomAppBar(title: "Turmas", showBack: false),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Buscar por nome da turma...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: const BorderSide(color: Colors.blueAccent),
                ),
                filled: true,
                fillColor: Colors.white,
              ),
              onChanged: _filterTurmas,
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
                  return const Center(
                    child: Text(
                      'Nenhuma turma encontrada.',
                      style: TextStyle(fontSize: 16),
                    ),
                  );
                }

                return ListView.builder(
                  padding: const EdgeInsets.symmetric(horizontal: 12),
                  itemCount: _filteredTurmas.length,
                  itemBuilder: (context, index) {
                    final turma = _filteredTurmas[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(vertical: 8),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                      elevation: 3,
                      child: ListTile(
                        contentPadding: const EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 12,
                        ),
                        leading: CircleAvatar(
                          backgroundColor: Colors.blue[100],
                          child: const Icon(Icons.groups, color: Colors.blue),
                        ),
                        title: Text(
                          turma.nome,
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
                              builder: (context) => AlunosPage(turma: turma),
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
