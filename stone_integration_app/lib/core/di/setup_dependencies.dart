import 'package:get_it/get_it.dart';
import 'package:stone_plugin/stone_plugin.dart';

import '../../data/datasources/plugin_datasource.dart';
import '../../data/repositories_impl/plugin_repository_impl.dart';
import '../../domain/datasource/i_plugin_datasource.dart';
import '../../domain/repositories/i_plugin_repository.dart';

final getIt = GetIt.instance;

class SetupDependencies {
  static void setup() {

    if (!getIt.isRegistered<IPluginRepository>()) {
      getIt.registerSingleton<IPluginRepository>(
        PluginRepositoryImpl(getIt<IPluginDataSource>()),
      );
    }
    if (!getIt.isRegistered<IPluginDataSource>()) {
      getIt.registerSingleton<IPluginDataSource>(
        PluginDataSource(StonePlugin()),
      );
    }
  }
}
