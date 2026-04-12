import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:stone_integration_app/ui/state/stone_state.dart';
import 'package:stone_integration_app/ui/state/stone_state_extention.dart';
import 'package:stone_integration_app/ui/widget/payment_pop_up.dart';

import '../domain/enum/stone_intent_enum.dart';
import 'cubit/stone_cubit.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _State();
}

class _State extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Stone SDK Example')),
      body: BlocConsumer<StoneCubit, PluginState>(
        listener: (context, state) {
          if (state is PluginSelectPayment) {
            showDialog(context: context, builder:
                (context) => PaymentPopUp(onPaymentSelected: (amount){
                  this.context.read<StoneCubit>().processPayment(amount);
                },)).then((v){
              context.read<StoneCubit>().selectPaymentCancel();
            });
          }
        },
        builder: (BuildContext context, PluginState state) {
          if (state is PluginLoading) {
            return const Center(
              child: CircularProgressIndicator(color: Colors.black),
            );
          }
          final label = switch (state) {
            PluginInitial() => 'Pronto para testar.',
            PluginLoading() => 'Chamando SDK...',
            PluginSelectPayment() => 'Selecione um pagamento',
            PluginProcessing(status: var s) => s.message,
            PluginSuccess(result: var r) => r ?? 'Sucesso!',
            PluginError(message: var m) => m,
            PluginState() => throw UnimplementedError(),
          };

          return Center(
            child: Column(
              spacing: 24,
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    label,
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                ),
                ...state.availableActions.map(
                  (intent) => ElevatedButton(
                    onPressed: state.isBusy
                        ? null
                        : () => context.read<StoneCubit>().onMainButtonPressed(
                            intent,
                          ),
                    child: Text(intent.customName.toUpperCase()),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
