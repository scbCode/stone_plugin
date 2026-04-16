import 'package:stone_plugin/data/models/payment_model.dart';

import '../../data/model/payment_model.dart';

abstract class IPluginDataSource {
  Future<String?> init();
  Future<String?> printReceipt();
  Future<bool> activateStonecode({required String stoneCode});
  Future<String?> payment({required PaymentModel paymentModel});
  Stream<String?> paymentStream();
}
