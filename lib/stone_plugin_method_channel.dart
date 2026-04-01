import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'models/payment_model.dart';
import 'stone_plugin_platform_interface.dart';

/// An implementation of [StonePluginPlatform] that uses method channels.
class MethodChannelStonePlugin extends StonePluginPlatform {
  /// The method channel used to interact with the native platform.

  @visibleForTesting
  final methodChannel = const MethodChannel('stone_plugin');
  final eventChannel = EventChannel('stone_plugin_stream_payment');

  @override
  Future<String?> init() async {
    try {
      final result = await methodChannel.invokeMethod<String>('init');
      return result;
    } on PlatformException catch (e) {
      return "Erro ao inicializar o SDK";
    } catch (e) {
      return "Erro desconhecido";
    }
  }

  @override
  Future<String?> printReceipt() async {
    try {
      final result = await methodChannel.invokeMethod<String>('printReceipt');
      return result;
    } on PlatformException catch (e) {
      return "Erro ao imprimir";
    } catch (e) {
      return "Erro desconhecido";
    }
  }

  @override
  Future<bool> activateStonecode({required String stoneCode}) async {
    try {
      final result = await methodChannel.invokeMethod<bool>('activateStoneCode',stoneCode);
      return result ?? false;
    } on PlatformException catch (e) {
      return false;
    } catch (e) {
      return false;
    }
  }

  @override
  Future<String?> payment({required PaymentModelPlatform paymentModel}) async {
    try {

      final result = await methodChannel.invokeMethod<String>(
        'payment',
        paymentModel.toMap()
      );

      return result;
    } on PlatformException catch (e) {
      return  ("Erro ao processar pagamento");
    } catch (e) {
      return ("Erro desconhecido");
    }
  }

  @override
  Stream paymentStream() {
    return eventChannel.receiveBroadcastStream();
  }

}
