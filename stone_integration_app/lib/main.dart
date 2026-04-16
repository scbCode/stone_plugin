import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:stone_integration_app/core/di/setup_dependencies.dart';
import 'package:stone_integration_app/ui/cubit/stone_cubit.dart';
import 'package:stone_integration_app/ui/home_page.dart';

import 'domain/repositories/i_plugin_repository.dart';

void main() {
  SetupDependencies.setup();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Stone App Flutter',
      theme: ThemeData(colorScheme: .fromSeed(seedColor: Colors.deepPurple)),
      home: BlocProvider(create: (context) => StoneCubit(repository:
          getIt<IPluginRepository>()
      ), child: HomePage()),
    );
  }
}
