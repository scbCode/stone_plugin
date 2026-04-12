enum StonePaymentStep {
  waitingCard("TRANSACTION_WAITING_CARD", "Aguardando o cartão ser inserido"),
  waitingPassword("TRANSACTION_WAITING_PASSWORD", "Aguardando a senha do cartão"),
  sending("TRANSACTION_SENDING", "Enviando a transação para a Stone"),
  removeCard("TRANSACTION_REMOVE_CARD", "Aguardando o cartão ser retirado"),
  cardRemoved("TRANSACTION_CARD_REMOVED", "Cartão removido"),
  reversingError("REVERSING_TRANSACTION_WITH_ERROR", "Transação com erro."),
  typeSelection("TRANSACTION_TYPE_SELECTION", "Escolha a modalidade no terminal"),
  waitingQrCode("TRANSACTION_WAITING_QRCODE_SCAN", "Aguarde a leitura do QR Code"),
  paymentSuccess("PAYMENT_SUCCESS", "Pagamento realizado com sucesso"),
  paymentError("PAYMENT_ERROR", "Erro ao processar o pagamento"),
  cancelled("CANCELLED", "Transação cancelada"),
  withError("WITH_ERROR", "Erro na transação"),
  unknown("UNKNOWN", "Processando...");

  final String value;
  final String message;

  const StonePaymentStep(this.value, this.message);

  bool get isError => this == StonePaymentStep.withError;

  static StonePaymentStep fromString(String? val) {
    return StonePaymentStep.values.firstWhere(
          (e) => e.value == val,
      orElse: () => StonePaymentStep.unknown,
    );
  }
}