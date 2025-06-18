import 'package:flutter/material.dart';
import '../../datasources/remote/turma_remote.dart';
import '../../models/turma.dart';

class TurmasPage extends StatefulWidget {
  const TurmasPage({super.key});

  @override
  State<TurmasPage> createState() => _TurmasPageState();
}

class _TurmasPageState extends State<TurmasPage> {
  late Future<List<Turma>> _turmasFuture;

  @override
  void initState() {
    super.initState();
    _turmasFuture = TurmaRemoteDataSource().getTurmas();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Turmas')),
      body: FutureBuilder<List<Turma>>(
        future: _turmasFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text('Erro: ${snapshot.error}'));
          }

          final turmas = snapshot.data ?? [];

          if (turmas.isEmpty) {
            return const Center(child: Text('Nenhuma turma encontrada.'));
          }

          return ListView.builder(
            itemCount: turmas.length,
            itemBuilder: (context, index) {
              final turma = turmas[index];
              return ListTile(
                title: Text(turma.nome),
                trailing: const Icon(Icons.arrow_forward_ios),
                onTap: () {
                  // Navigator.push(context, MaterialPageRoute(builder: (_) => DisciplinasPage(turma: turma)));
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Turma selecionada: ${turma.nome}')),
                  );
                },
              );
            },
          );
        },
      ),
    );
  }
}
