import '../../data/model/payment_model.dart';

abstract class IRepository {
  Future<String?> init();

  Future<String?> printReceipt();

  Future<bool> activateStonecode({required String stoneCode});

  Future<String?> payment({required PaymentModel paymentModel});

  Stream<String?> paymentStream();
}