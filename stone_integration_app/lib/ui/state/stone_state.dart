import 'package:equatable/equatable.dart';
import 'package:stone_integration_app/enum/stone_flags.dart';

import '../../enum/stone_intent_enum.dart';
import '../../enum/stone_payment_step.dart';

sealed class PluginState extends Equatable {
  @override
  List<Object?> get props => [];
}

class PluginInitial extends PluginState {
  final String? message;

  PluginInitial([this.message]);

  @override
  List<Object?> get props => [message];
}

class PluginSelectPayment extends PluginState {
  PluginSelectPayment();

  @override
  List<Object?> get props => [];
}

class PluginLoading extends PluginState {}

class PluginSuccess extends PluginState {
  final String? result;
  final StoneFlags? flag;

  PluginSuccess(this.result, {this.flag});

  @override
  List<Object?> get props => [flag, result];
}

class PluginError extends PluginState {
  final String message;

  PluginError(this.message);

  @override
  List<Object?> get props => [message];
}

class PluginProcessing extends PluginState {
  final StonePaymentStep status;

  PluginProcessing(this.status);

  @override
  List<Object?> get props => [status];
}

