import 'package:stone_plugin/stone_plugin.dart';

import '../../data/model/payment_model.dart';
import '../../domain/repositories/i_repository.dart';

class RepositoryImpl implements IRepository {
  final StonePlugin _plugin;

  RepositoryImpl({StonePlugin? plugin}) : _plugin = plugin ?? StonePlugin();

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
  Future<String?> payment({required PaymentModel paymentModel}) {
    return _plugin.payment(paymentModel: paymentModel);
  }

  @override
  Stream<String?> paymentStream() {
    return _plugin.paymentStream().map((event) => event?.toString());
  }
}