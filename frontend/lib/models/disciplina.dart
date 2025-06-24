class Disciplina {
  final int id;
  final String nome;

  Disciplina({required this.id, required this.nome});

  factory Disciplina.fromJson(Map<String, dynamic> json) {
    return Disciplina(
      id: json['id'],
      nome: json['nome'],
    );
  }
}
