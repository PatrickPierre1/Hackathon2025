class Aluno {
  final int id;
  final String nome;
  final String ra;

  Aluno({required this.id, required this.nome, required this.ra});

  factory Aluno.fromJson(Map<String, dynamic> json) {
    return Aluno(
      id: json['id'],
      nome: json['nome'],
      ra: json['ra'],
    );
  }
}
