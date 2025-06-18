import '../datasources/remote/login_remote.dart';

class LoginService {
  final LoginRemoteDataSource _remote = LoginRemoteDataSource();

  Future<String?> login(String username, String password) {
    return _remote.login(username, password);
  }
}
