import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:stone_integration_app/cubit/stone_cubit.dart';
import 'package:stone_integration_app/ui/home_page.dart';
import 'package:stone_plugin/stone_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Stone App Flutter',
      theme: ThemeData(colorScheme: .fromSeed(seedColor: Colors.deepPurple)),
      home: BlocProvider(create: (context) => StoneCubit(), child: HomePage()),
    );
  }
}
