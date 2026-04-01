
import '../stone_payment_status_enum.dart';

class StonePaymentModel {
  StonePaymentStatus status;

  StonePaymentModel(this.status, this.message);

  String? message;
}