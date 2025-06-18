import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'ui/pages/login_page.dart';
import 'ui/pages/placeholder_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Future<String?>? _futureUserName;

  @override
  void initState() {
    super.initState();
    _futureUserName = _loadUserName();
  }

  Future<String?> _loadUserName() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('user_name');
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Gabarito OCR',
      theme: ThemeData(primarySwatch: Colors.indigo),
      home: FutureBuilder<String?>(
        future: _futureUserName,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Scaffold(
              body: Center(child: CircularProgressIndicator()),
            );
          }

          final name = snapshot.data;
          if (name != null && name.isNotEmpty) {
            return PlaceholderPage(username: name);
          } else {
            return const LoginPage();
          }
        },
      ),
    );
  }
}
