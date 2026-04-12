import 'package:stone_integration_app/ui/state/stone_state.dart';

import '../../domain/enum/stone_flags.dart';
import '../../domain/enum/stone_intent_enum.dart';
import '../../domain/enum/stone_payment_step.dart';

extension StoneStateExt on PluginState {
  bool get isBusy => this is PluginLoading;

  List<StoneIntent> get availableActions {
    return switch (this) {
      PluginLoading() => [],

      PluginSelectPayment() => [],

      PluginSuccess(flag: StoneFlags thisFlag) =>
      thisFlag == StoneFlags.initialized
          ? [StoneIntent.activateStonecode]
          : [StoneIntent.amountSelector],

      PluginProcessing(status: StonePaymentStep thisStep) =>
      thisStep == StonePaymentStep.cancelled
          ? [StoneIntent.amountSelector]
          : [StoneIntent.cancel],

      PluginSuccess(flag: null) ||
      PluginError() ||
      PluginInitial() => [StoneIntent.initialize],
    };
  }
}
