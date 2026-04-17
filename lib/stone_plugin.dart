import 'domain/params/payment_params.dart';
import 'domain/interface/stone_plugin_platform_interface.dart';

class StonePlugin {
  Future<String?> init() {
    return StonePluginPlatform.instance.init();
  }

  Future<String?> printReceipt() {
    return StonePluginPlatform.instance.printReceipt();
  }

  Future<bool> activateStonecode({required String stoneCode})=>
      StonePluginPlatform.instance.activateStonecode(stoneCode: stoneCode);

  Future<String?> payment({required PaymentParams params}) {
    return StonePluginPlatform.instance.payment(params: params);
  }

  Stream paymentStream() {
    return StonePluginPlatform.instance.paymentStream();
  }
}
