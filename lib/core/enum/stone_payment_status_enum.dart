enum StonePaymentStatus {
  initializing('Iniciando SDK...'),
  waiting('Aguardando cartão...'),
  password('Aguardando senha...'),
  processing('Processando transação...'),
  success('Pagamento aprovado!'),
  genericError('Erro inesperado'),
  passwordIncorrect('Senha incorreta'),
  invalidCard('Cartão inválido ou recusado'),
  invalidAmount('Valor inválido para a operação'),
  unauthorized('Transação não autorizada');

  final String message;
  const StonePaymentStatus(this.message);

  static StonePaymentStatus fromString(String value) {
    return StonePaymentStatus.values.firstWhere(
          (e) => e.name == value,
      orElse: () => StonePaymentStatus.genericError,
    );
  }
}