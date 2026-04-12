import 'package:get_it/get_it.dart';

import '../../data/repositories_impl/repository_impl.dart';
import '../../domain/repositories/i_repository.dart';

final getIt = GetIt.instance;

class SetupDependencies {
  static void setup() {
    if (!getIt.isRegistered<IRepository>()) {
      getIt.registerSingleton<IRepository>(RepositoryImpl());
    }
  }
}
