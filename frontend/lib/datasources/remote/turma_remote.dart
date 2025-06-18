import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../models/turma.dart';

class TurmaRemoteDataSource {
  Future<List<Turma>> getTurmas() async {
    final response = await http.get(Uri.parse('https://685238140594059b23cca402.mockapi.io/api/turmas'));

    if (response.statusCode == 200) {
      final List<dynamic> jsonList = json.decode(response.body);
      return jsonList.map((e) => Turma.fromJson(e)).toList();
    } else {
      throw Exception('Falha ao carregar turmas');
    }
  }
}
