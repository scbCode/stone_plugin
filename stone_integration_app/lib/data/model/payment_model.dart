import 'dart:collection';

import 'package:stone_plugin/models/payment_model.dart';

class PaymentModel implements PaymentModelPlatform {
  final String type;
  final String amount;
  final String stoneCode;

  PaymentModel({
    required this.type,
    required this.amount,
    required this.stoneCode,
  });

  factory PaymentModel.fromMap(Map<String, Object> map) {
    return PaymentModel(
      type: map['type'] as String,
      amount: map['amount'] as String,
      stoneCode: map['stoneCode'] as String,
    );
  }

  @override
  HashMap<String, String> toMap() => HashMap<String, String>.from({
    'type': type,
    'amount': amount,
    'stoneCode': stoneCode,
  });
}
