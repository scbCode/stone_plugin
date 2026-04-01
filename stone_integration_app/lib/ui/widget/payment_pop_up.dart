import 'package:flutter/material.dart';

typedef TransactionItem = ({String label, double amount});

class PaymentPopUp extends StatelessWidget {
  Function onPaymentSelected;

  PaymentPopUp({super.key, required this.onPaymentSelected});

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Pagamento'),
      content: const Text('Selecione um pagamento'),
      actions: [
        ..._payments.map(
          (item) => FilledButton(
            onPressed: () {
              onPaymentSelected(item.amount);
              Navigator.pop(context);
            },
            child: Text(item.label),
          ),
        ),
      ],
    );
  }

  final List<TransactionItem> _payments = [
    (label: 'R\$2,00', amount: 2),
    (label: 'R\$5,00', amount: 5),
  ];
}
