import 'package:flutter_test/flutter_test.dart';
import 'package:stone_plugin/stone_plugin.dart';
import 'package:stone_plugin/stone_plugin_platform_interface.dart';
import 'package:stone_plugin/stone_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockStonePluginPlatform
    with MockPlatformInterfaceMixin
    implements StonePluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final StonePluginPlatform initialPlatform = StonePluginPlatform.instance;

  test('$MethodChannelStonePlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelStonePlugin>());
  });

  test('getPlatformVersion', () async {
    StonePlugin stonePlugin = StonePlugin();
    MockStonePluginPlatform fakePlatform = MockStonePluginPlatform();
    StonePluginPlatform.instance = fakePlatform;

    expect(await stonePlugin.init(), '42');
  });
}
