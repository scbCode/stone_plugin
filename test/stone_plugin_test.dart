import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:stone_plugin/data/datasources/channel/stone_plugin_method_channel.dart';
import 'package:stone_plugin/data/models/payment_model.dart';
import 'package:stone_plugin/stone_plugin.dart';
import 'package:stone_plugin/stone_plugin_platform_interface.dart';

class _MockStonePlatform extends StonePluginPlatform
    with MockPlatformInterfaceMixin {
  String? initResult;

  @override
  Future<String?> init() async => initResult;

  @override
  Future<String?> printReceipt() async => 'ok';

  @override
  Future<bool> activateStonecode({required String stoneCode}) async => true;

  @override
  Future<String?> payment({required PaymentModelPlatform paymentModel}) async =>
      'paid';

  @override
  Stream paymentStream() => const Stream.empty();
}

class _BadStonePlatform implements StonePluginPlatform {
  @override
  Future<String?> init() async => 'bad';

  @override
  Future<String?> printReceipt() async => 'bad';

  @override
  Future<bool> activateStonecode({required String stoneCode}) async => false;

  @override
  Future<String?> payment({required PaymentModelPlatform paymentModel}) async =>
      'bad';

  @override
  Stream paymentStream() => const Stream.empty();
}

void main() {
  group('StonePluginPlatform token verification', () {
    test('default instance is MethodChannelStonePlugin', () {
      expect(StonePluginPlatform.instance, isInstanceOf<MethodChannelStonePlugin>());
    });

    test('accepts instance that extends platform with token', () {
      final platform = _MockStonePlatform();
      StonePluginPlatform.instance = platform;

      expect(StonePluginPlatform.instance, same(platform));
    });

    test('rejects instance that only implements interface', () {
      expect(
        () => StonePluginPlatform.instance = _BadStonePlatform(),
        throwsA(isA<AssertionError>()),
      );
    });
  });

  test('StonePlugin delegates calls to current platform instance', () async {
    final plugin = StonePlugin();
    final platform = _MockStonePlatform()..initResult = 'ready';
    StonePluginPlatform.instance = platform;

    final result = await plugin.init();

    expect(result, 'ready');
  });
}
