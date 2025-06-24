import 'package:flutter/material.dart';
import '../../services/user_preferences.dart';
import 'package:frontend/ui/pages/login_page.dart';

class PlaceholderPage extends StatelessWidget {
  final String username;

  const PlaceholderPage({super.key, required this.username});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Bem-vindo!'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () async {
              final prefs = UserPreferences();
              await prefs.logout();

              if (context.mounted) {
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (_) => const LoginPage()),
                );
              }
            },
          ),
        ],
      ),
      body: Center(
        child: Text(
          'Olá, $username!',
          style: const TextStyle(fontSize: 24),
        ),
      ),
    );
  }
}
