import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../models/disciplina.dart';

class DisciplinaRemoteDataSource {
  Future<List<Disciplina>> getDisciplinas() async {
    final response = await http.get(Uri.parse('https://685238140594059b23cca402.mockapi.io/api/disciplinas'));

    if (response.statusCode == 200) {
      final List<dynamic> jsonList = json.decode(response.body);
      return jsonList.map((e) => Disciplina.fromJson(e)).toList();
    } else {
      throw Exception('Falha ao carregar disciplinas');
    }
  }
}
