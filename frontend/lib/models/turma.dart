import 'aluno_page.dart';

class Turma {
  final int id;
  final String nome;
  final List<Aluno> alunos;

  Turma({required this.id, required this.nome, required this.alunos});

  factory Turma.fromJson(Map<String, dynamic> json) {
    final List<dynamic> alunosJson = json['alunos'] ?? [];
    final alunos = alunosJson.map((e) => Aluno.fromJson(e)).toList();

    return Turma(
      id: json['id'],
      nome: json['nome'],
      alunos: alunos,
    );
  }
}

class Aluno {
  static fromJson(e) {}
}
