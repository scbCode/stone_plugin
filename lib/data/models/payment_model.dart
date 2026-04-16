abstract class PaymentModelPlatform {
  final String type;
  final String amount;
  final String stoneCode;

  PaymentModelPlatform({
    required this.type,
    required this.amount,
    required this.stoneCode,
  });

  Map<String, Object> toMap();
}
