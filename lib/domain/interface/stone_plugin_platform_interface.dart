import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:stone_plugin/core/utils/consts.dart';

import '../../data/datasources/channel/stone_plugin_method_channel.dart';
import '../params/payment_params.dart';

abstract class StonePluginPlatform extends PlatformInterface {
  StonePluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static StonePluginPlatform _instance = MethodChannelStonePlugin(
    methodChannel: MethodChannel(Consts.channelName),
    eventChannel: EventChannel(Consts.eventChannelStreamPayment),
  );

  /// The default instance of [StonePluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelStonePlugin].
  static StonePluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [StonePluginPlatform] when
  /// they register themselves.
  static set instance(StonePluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> init() {
    throw UnimplementedError('init() has not been implemented.');
  }

  Future<String?> printReceipt() {
    throw UnimplementedError('printReceipt() has not been implemented.');
  }

  Future<bool> activateStonecode({required String stoneCode}) =>
      throw UnimplementedError('activateStonecode() has not been implemented.');

  Future<String?> payment({required PaymentParams params}) =>
      throw UnimplementedError('payment() has not been implemented.');

  Stream paymentStream() {
    throw UnimplementedError('paymentStream() has not been implemented.');
  }
}
