
import 'package:stone_plugin/domain/params/payment_params.dart';

abstract class IPluginDataSource {
  Future<String?> init();
  Future<String?> printReceipt();
  Future<bool> activateStonecode({required String stoneCode});
  Future<String?> payment({required PaymentParams params});
  Stream<String?> paymentStream();
}
