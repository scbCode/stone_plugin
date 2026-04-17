import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import '../../../domain/interface/stone_plugin_platform_interface.dart';
import '../../../domain/params/payment_params.dart';
import '../../mappers/payment_params_mapper.dart';

class MethodChannelStonePlugin extends StonePluginPlatform {

  MethodChannelStonePlugin({required this.methodChannel, required this.eventChannel});
  final MethodChannel methodChannel;
  final EventChannel eventChannel;

  void _logChannelError(
    String method,
    Object error,
    StackTrace stackTrace,
  ) {
    debugPrint('[StonePlugin][$method] $error');
    debugPrintStack(stackTrace: stackTrace);
  }

  @override
  Future<String?> init() async {
    try {
      final result = await methodChannel.invokeMethod<String>('init');
      return result;
    } on PlatformException catch (e, s) {
      _logChannelError('init', e, s);
      return 'Erro ao inicializar o SDK';
    } catch (e, s) {
      _logChannelError('init', e, s);
      return 'Erro desconhecido';
    }
  }

  @override
  Future<String?> printReceipt() async {
    try {
      final result = await methodChannel.invokeMethod<String>('printReceipt');
      return result;
    } on PlatformException catch (e, s) {
      _logChannelError('printReceipt', e, s);
      return 'Erro ao imprimir';
    } catch (e, s) {
      _logChannelError('printReceipt', e, s);
      return 'Erro desconhecido';
    }
  }

  @override
  Future<bool> activateStonecode({required String stoneCode}) async {
    try {
      final result = await methodChannel.invokeMethod<bool>(
        'activateStoneCode',
        stoneCode,
      );
      return result ?? false;
    } on PlatformException catch (e, s) {
      _logChannelError('activateStonecode', e, s);
      return false;
    } catch (e, s) {
      _logChannelError('activateStonecode', e, s);
      return false;
    }
  }

  @override
  Future<String?> payment({required PaymentParams params}) async {
    try {
      final result = await methodChannel.invokeMethod<String>(
        'payment',
        params.toMap(),
      );

      return result;
    } on PlatformException catch (e, s) {
      _logChannelError('payment', e, s);
      return 'Erro ao processar pagamento';
    } catch (e, s) {
      _logChannelError('payment', e, s);
      return 'Erro desconhecido';
    }
  }

  @override
  Stream paymentStream() {
    return eventChannel.receiveBroadcastStream();
  }
}
