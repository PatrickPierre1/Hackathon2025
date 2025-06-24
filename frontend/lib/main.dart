import 'package:flutter/material.dart';
import 'package:frontend/ui/pages/turma_page.dart';

void main() {
  runApp(
    const MaterialApp(
      home: TurmasPage(),
      title: "Gabarito OCR",
      debugShowCheckedModeBanner: false,
    ),
  );
}