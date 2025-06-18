import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class UserPreferences {
  static const _keyUserName = 'user_name';
  static const _keySavedUsername = 'saved_username';
  static const _keySavedCredentials = 'saved_credentials';

  Future<void> saveUser(String name) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyUserName, name);
    await prefs.setString(_keySavedUsername, name);
  }

  Future<void> saveCredentials(String username, String password) async {
    final prefs = await SharedPreferences.getInstance();
    Map<String, String> credentials = {};

    final jsonString = prefs.getString(_keySavedCredentials);
    if (jsonString != null) {
      credentials = Map<String, String>.from(jsonDecode(jsonString));
    }

    credentials[username] = password;

    await prefs.setString(_keySavedCredentials, jsonEncode(credentials));
    await prefs.setString(_keySavedUsername, username);
  }

  Future<Map<String, String>> getSavedCredentials() async {
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_keySavedCredentials);
    if (jsonString == null) return {};
    return Map<String, String>.from(jsonDecode(jsonString));
  }

  Future<String?> getSavedUsername() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_keySavedUsername);
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_keyUserName);
    await prefs.remove(_keySavedUsername);
  }
}
