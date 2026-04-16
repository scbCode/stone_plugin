import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:stone_plugin/data/datasources/channel/stone_plugin_method_channel.dart';
import 'package:stone_plugin/data/models/payment_model.dart';

class _PaymentModelStub extends PaymentModelPlatform {
  _PaymentModelStub({required super.type, required super.amount, required super.stoneCode});

  @override
  Map<String, Object> toMap() => {
        'type': type,
        'amount': amount,
        'stoneCode': stoneCode,
      };
}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  final platform = MethodChannelStonePlugin();
  const channel = MethodChannel('stone_plugin');


  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('init returns native result', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall call) async {
      expect(call.method, 'init');
      return 'SDK Inicializado com sucesso';
    });

    expect(await platform.init(), 'SDK Inicializado com sucesso');
  });

  test('init returns fallback message on PlatformException', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall call) async {
      throw PlatformException(code: 'error');
    });

    expect(await platform.init(), 'Erro ao inicializar o SDK');
  });

  test('activateStonecode forwards args and returns bool', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall call) async {
      expect(call.method, 'activateStoneCode');
      expect(call.arguments, '12345678');
      return true;
    });

    expect(
      await platform.activateStonecode(stoneCode: '12345678'),
      isTrue,
    );
  });

  test('payment forwards map and returns native payload', () async {
    final payment = _PaymentModelStub(
      type: 'credit',
      amount: '1000',
      stoneCode: '12345678',
    );

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall call) async {
      expect(call.method, 'payment');
      expect(call.arguments, payment.toMap());
      return 'approved';
    });

    expect(await platform.payment(paymentModel: payment), 'approved');
  });

  test('payment returns fallback message on PlatformException', () async {
    final payment = _PaymentModelStub(
      type: 'debit',
      amount: '1000',
      stoneCode: '12345678',
    );

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall call) async {
      throw PlatformException(code: 'payment_error');
    });

    expect(
      await platform.payment(paymentModel: payment),
      'Erro ao processar pagamento',
    );
  });
}
