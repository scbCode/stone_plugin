import 'package:stone_plugin/domain/params/payment_params.dart';

extension PaymentParamsMapper on PaymentParams {
  Map<String, String> toMap() => {
    'type': type,
    'amount': amount,
    'stoneCode': stoneCode,
  };
}
