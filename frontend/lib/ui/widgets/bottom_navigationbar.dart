import 'package:flutter/material.dart';

class CustomBottomBar extends StatelessWidget {
  final VoidCallback onLogout;

  const CustomBottomBar({super.key, required this.onLogout});

  @override
  Widget build(BuildContext context) {
    return BottomAppBar(
      shape: const CircularNotchedRectangle(),
      notchMargin: 8.0,
      child: SizedBox(
        height: 60,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            IconButton(
              icon: const Icon(Icons.logout, color: Colors.redAccent,),
              tooltip: 'Logout',
              onPressed: onLogout,

            ),
            const SizedBox(width: 16),
          ],
        ),
      ),
    );
  }
}
