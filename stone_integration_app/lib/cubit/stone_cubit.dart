import 'dart:async';

import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:stone_integration_app/cubit/stone_state.dart';
import 'package:stone_integration_app/enum/stone_flags.dart';
import 'package:stone_integration_app/enum/stone_intent_enum.dart';
import 'package:stone_integration_app/enum/stone_payment_step.dart';
import 'package:stone_plugin/stone_plugin.dart';

import 'model/payment_model.dart';

class StoneCubit extends Cubit<PluginState> {
  //O ideal é usar um singleton para instanciar o plugin
  //mas para o exemplo vamos deixar assim
  final StonePlugin _plugin = StonePlugin();
  StreamSubscription? _paymentStream;

  StoneCubit() : super(PluginInitial());

  Future<void> init() async {
    emit(PluginLoading());
    try {
      final res = await _plugin.init();
      emit(PluginSuccess(res, flag: StoneFlags.initialized));
    } catch (e) {
      emit(PluginError(e.toString()));
    }
  }

  Future<void> printReceipt() async {
    emit(PluginLoading());
    try {
      await _plugin.printReceipt();
      emit(PluginSuccess('Impressão OK - Código 00'));
    } catch (e) {
      emit(PluginError(e.toString()));
    }
  }

  Future<void> activateStonecode() async {
    emit(PluginLoading());
    try {
      final result = await _plugin.activateStonecode(stoneCode: '750613196');
      if (!result) {
        emit(PluginError('Falha ao ativar Stonecode'));
        return;
      }
      emit(PluginSuccess('Stonecode ativado', flag: StoneFlags.stonecodeActivated));
    } catch (e) {
      emit(PluginError(e.toString()));
    }
  }

  Future<void> processPayment(double amount) async {
    emit(PluginLoading());
    try {
      await _plugin.payment(
        paymentModel: PaymentModel(
          type: 'CREDIT_CARD',
          amount: (amount * 100).toStringAsFixed(0),
          stoneCode: '750613196',
        ),
      );

      emit(PluginProcessing(StonePaymentStep.waitingCard));
      paymentStream();
    } catch (e) {
      emit(PluginError(e.toString()));
    }
  }

  void paymentStream() {
    _paymentStream = _plugin.paymentStream().listen((onDate) {
      StonePaymentStep step = StonePaymentStep.fromString(onDate);

      if (step.isError) {
        emit(PluginError(step.message));
        return;
      }

      if (step == StonePaymentStep.paymentSuccess) {
        emit(PluginSuccess(step.message,flag: StoneFlags.success));
        _paymentStream?.cancel();
        return;
      }

      emit(PluginProcessing(StonePaymentStep.fromString(onDate)));
    });
    _paymentStream?.onError((error, stackTrace) {
      emit(PluginError('Erro ao processar pagamento'));
    });
  }

  void paymentCancel() {
    _paymentStream?.cancel();
    emit(PluginInitial());
  }

  void selectPaymentCancel() {
    emit(PluginSuccess('Stonecode ativado', flag: StoneFlags.stonecodeActivated));
  }

  void onMainButtonPressed(StoneIntent intent) {
    switch (intent) {
      case StoneIntent.initialize:
        init();
      case StoneIntent.print:
        printReceipt();
      case StoneIntent.amountSelector:
        emit(PluginSelectPayment());
      case StoneIntent.cancel:
        paymentCancel();
      case StoneIntent.activateStonecode:
        activateStonecode();
      case StoneIntent.none:
        throw UnimplementedError();
    }
  }
}
