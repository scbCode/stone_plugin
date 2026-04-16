import 'package:stone_integration_app/data/model/payment_model.dart';
import 'package:stone_plugin/data/models/payment_model.dart';
import 'package:stone_plugin/stone_plugin.dart';

import '../../domain/datasource/i_plugin_datasource.dart';

class PluginDataSource implements IPluginDataSource {
  final StonePlugin _plugin;

  PluginDataSource(this._plugin);

  @override
  Future<bool> activateStonecode({required String stoneCode}) async {
    return await _plugin.activateStonecode(stoneCode: stoneCode);
  }

  @override
  Future<String?> init() async {
    return await _plugin.init();
  }

  @override
  Future<String?> payment({required PaymentModel paymentModel}) async {
    return await _plugin.payment(paymentModel: paymentModel);
  }

  @override
  Stream<String?> paymentStream() {
    return _plugin.paymentStream().map((event) => event?.toString());
  }

  @override
  Future<String?> printReceipt() async {
    return await _plugin.printReceipt();
  }
}

