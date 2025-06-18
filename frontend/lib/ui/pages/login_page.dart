import 'package:flutter/material.dart';
import '../../services/login_service.dart';
import '../../services/user_preferences.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _userController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _obscurePassword = true;

  final LoginService _loginService = LoginService();
  final UserPreferences _userPrefs = UserPreferences();

  bool _loading = false;
  String? _error;

  Map<String, String> _savedCredentials = {};
  List<String> _usernames = [];

  @override
  void initState() {
    super.initState();
    _loadSavedLogin();
  }

  Future<void> _loadSavedLogin() async {
    final credentials = await _userPrefs.getSavedCredentials();
    final lastUsername = await _userPrefs.getSavedUsername();

    setState(() {
      _savedCredentials = credentials;
      _usernames = credentials.keys.toList();

      if (lastUsername != null) {
        _userController.text = lastUsername;
        _passwordController.text = credentials[lastUsername] ?? '';
      }
    });
  }

  Future<void> _handleLogin() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    final username = _userController.text.trim();
    final password = _passwordController.text.trim();

    final name = await _loginService.login(username, password);

    if (name != null) {
      await _userPrefs.saveUser(name);
      await _userPrefs.saveCredentials(username, password);

      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Login realizado como $name')),
      );

      // Redirecionamento pode ser feito aqui
    } else {
      setState(() {
        _error = 'Usuário ou senha inválidos';
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff4f6f9),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            const SizedBox(height: 60),
            const Icon(Icons.school, size: 80, color: Colors.indigo),
            const SizedBox(height: 20),
            const Text(
              'Bem-vindo de volta!',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            const Text(
              'Faça login para acessar sua conta',
              style: TextStyle(fontSize: 16, color: Colors.grey),
            ),
            const SizedBox(height: 30),

            // Campo de usuário com autocompletar
            Align(
              alignment: Alignment.centerLeft,
              child: Text("Usuário", style: TextStyle(fontWeight: FontWeight.w600)),
            ),
            const SizedBox(height: 8),
            Autocomplete<String>(
              optionsBuilder: (TextEditingValue textEditingValue) {
                if (textEditingValue.text.isEmpty) {
                  return const Iterable<String>.empty();
                }
                return _usernames.where((username) =>
                    username.toLowerCase().contains(textEditingValue.text.toLowerCase()));
              },
              onSelected: (String selection) {
                _userController.text = selection;
                _passwordController.text = _savedCredentials[selection] ?? '';
              },
              fieldViewBuilder: (context, controller, focusNode, onEditingComplete) {
                controller.text = _userController.text;
                return TextField(
                  controller: controller,
                  focusNode: focusNode,
                  onEditingComplete: onEditingComplete,
                  decoration: InputDecoration(
                    prefixIcon: const Icon(Icons.person),
                    hintText: 'Digite seu usuário',
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
                    filled: true,
                    fillColor: Colors.white,
                  ),
                  onChanged: (text) {
                    _userController.text = text;
                  },
                );
              },
            ),
            const SizedBox(height: 20),

            // Campo de senha
            Align(
              alignment: Alignment.centerLeft,
              child: Text("Senha", style: TextStyle(fontWeight: FontWeight.w600)),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _passwordController,
              obscureText: _obscurePassword,
              decoration: InputDecoration(
                prefixIcon: const Icon(Icons.lock),
                suffixIcon: IconButton(
                  icon: Icon(
                    _obscurePassword ? Icons.visibility_off : Icons.visibility,
                  ),
                  onPressed: () {
                    setState(() {
                      _obscurePassword = !_obscurePassword;
                    });
                  },
                ),
                hintText: 'Digite sua senha',
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
                filled: true,
                fillColor: Colors.white,
              ),
            ),
            const SizedBox(height: 20),

            if (_error != null)
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Text(_error!, style: TextStyle(color: Colors.red)),
              ),

            SizedBox(
              width: double.infinity,
              child: ElevatedButton.icon(
                icon: _loading
                    ? const SizedBox(
                  width: 16,
                  height: 16,
                  child: CircularProgressIndicator(
                    strokeWidth: 2,
                    color: Colors.white,
                  ),
                )
                    : const Icon(Icons.rocket_launch_rounded),
                label: const Text('Entrar no Sistema'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.indigo,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  textStyle: const TextStyle(fontSize: 16),
                ),
                onPressed: _loading ? null : _handleLogin,
              ),
            ),

            const SizedBox(height: 50),

            // Parte institucional
            Column(
              children: [
                Image.asset('assets/logo.jpeg', height: 60),
                const SizedBox(height: 10),
                const Text(
                  'A melhor experiência acadêmica',
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: Colors.indigo,
                  ),
                  textAlign: TextAlign.center,
                ),
                const Text(
                  'que você já teve na sua vida.',
                  style: TextStyle(fontSize: 14),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
