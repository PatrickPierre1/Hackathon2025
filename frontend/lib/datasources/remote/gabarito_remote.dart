import 'dart:convert';
import 'package:http/http.dart' as http;

class GabaritoRemoteDataSource {
  Future<void> enviarRespostas({
    required String alunoId,
    required String provaId,
    required Map<int, String> respostas,
  }) async {
    final url = Uri.parse('https://suaapi.com.br/api/gabarito');

    final body = {
      'alunoId': alunoId,
      'provaId': provaId,
      'respostas': respostas.map((k, v) => MapEntry(k.toString(), v)),
    };

    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(body),
    );

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception('Erro ao enviar gabarito: ${response.statusCode}');
    }
  }
}
