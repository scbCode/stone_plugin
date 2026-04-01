import 'models/payment_model.dart';
import 'stone_plugin_platform_interface.dart';

class StonePlugin {
  Future<String?> init() {
    return StonePluginPlatform.instance.init();
  }

  Future<String?> printReceipt() {
    return StonePluginPlatform.instance.printReceipt();
  }

  Future<bool> activateStonecode({required String stoneCode})=>
      StonePluginPlatform.instance.activateStonecode(stoneCode: stoneCode);

  Future<String?> payment({required PaymentModelPlatform paymentModel}) {
    return StonePluginPlatform.instance.payment(paymentModel: paymentModel);
  }

  Stream paymentStream() {
    return StonePluginPlatform.instance.paymentStream();
  }
}
