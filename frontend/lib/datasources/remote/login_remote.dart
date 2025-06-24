class LoginRemoteDataSource {
  Future<String?> login(String username, String password) async {
    await Future.delayed(Duration(seconds: 1));

    // Administrador ou professor
    if (username == 'admin' && password == 'admin') {
      return 'Administrador';
    }

    // Aluno
    if (username == '12345678900' && password == '12345') {
      return 'Aluno';
    }

    return null;
  }
}
