
import 'package:stone_plugin/domain/params/payment_params.dart';

import '../../domain/datasource/i_plugin_datasource.dart';
import '../../domain/repositories/i_plugin_repository.dart';

class PluginRepositoryImpl implements IPluginRepository {
  final IPluginDataSource _plugin;

  PluginRepositoryImpl(IPluginDataSource plugin) : _plugin = plugin;

  @override
  Future<String?> init() {
    return _plugin.init();
  }

  @override
  Future<String?> printReceipt() {
    return _plugin.printReceipt();
  }

  @override
  Future<bool> activateStonecode({required String stoneCode}) {
    return _plugin.activateStonecode(stoneCode: stoneCode);
  }

  @override
  Future<String?> payment({required PaymentParams params}) {
    return _plugin.payment(params: params);
  }

  @override
  Stream<String?> paymentStream() {
    return _plugin.paymentStream().map((event) => event?.toString());
  }
}
