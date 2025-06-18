import 'dart:async';
// import 'dart:convert';
// import 'package:http/http.dart' as http;
import '../../models/turma.dart';

class TurmaRemoteDataSource {
  Future<List<Turma>> getTurmas() async {
    // Deixando o codigo mockado para usar sem a API
    await Future.delayed(const Duration(seconds: 1));

    return [
      Turma(id: 1, nome: 'Turma 5ºA'),
      Turma(id: 2, nome: 'Turma 5ºB'),
      Turma(id: 3, nome: 'Turma 6ºA'),
    ];

    /*
    final response = await http.get(Uri.parse('https://localhost:8000/api/turmas'));

    if (response.statusCode == 200) {
      final List<dynamic> jsonList = json.decode(response.body);
      return jsonList.map((e) => Turma.fromJson(e)).toList();
    } else {
      throw Exception('Falha ao carregar turmas');
    }
    */
  }
}
