import 'package:flutter/cupertino.dart';

@immutable
class PaymentParams {
  final String type;
  final String amount;
  final String stoneCode;

  const PaymentParams({
    required this.type,
    required this.amount,
    required this.stoneCode,
  });

}
