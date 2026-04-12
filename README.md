
# Stone Smart POS Flutter Plugin 💳 (Demo) DART 3

[![Flutter](https://img.shields.io/badge/Flutter-Plugin-02569B?logo=flutter&logoColor=white)](https://flutter.dev)
[![Stone SDK](https://img.shields.io/badge/Stone_SDK-v4.15.0+-00A9E0?logo=android&logoColor=white)](https://docs.stone.com.br/)

Plugin Flutter (demo) para integração com terminais **Smart POS** da Stone, com comunicação reativa via `MethodChannel` e `EventChannel`, separação em camadas e gateway desacoplado da SDK nativa.

## 🛠 Ambiente e Stack Atual

### Core
- **Dart SDK:** `^3.11.4` (`pubspec.yaml`)
- **Java:** `17`
- **Gradle Wrapper:** `8.9` (`android/gradle/wrapper/gradle-wrapper.properties`)

### Android
- **Android Gradle Plugin (AGP):** `8.7.3` (`android/build.gradle.kts`)
- **Kotlin Gradle Plugin:** `2.0.21` (`android/build.gradle.kts`)
- **compileSdk:** `36`
- **minSdk:** `24`
- **Stone SDK (default):** `4.15.0` (configurável por propriedade)

---

## 🏗 Arquitetura Atual

### Camada Flutter (Dart)
- `lib/stone_plugin.dart`: Facade pública do plugin.
- `lib/stone_plugin_method_channel.dart`: implementação via canais.
- `lib/stone_plugin_platform_interface.dart`: contrato + `PlatformInterface.verifyToken(instance, _token)`.

### Camada Android (Java)
- `android/.../stone_plugin/StonePlugin.java`
    - Entry point do plugin.
    - Implementa `FlutterPlugin`, `MethodCallHandler` e `ActivityAware`.
    - Cria e registra:
        - `MethodChannel("stone_plugin")`
        - `EventChannel("stone_plugin_stream_payment")`
    - Injeta `StoneSdkAdapter` em UseCases.
- UseCases (`domain/usecases`):
    - `StoneInitUseCase`
    - `ActivateStonecodeUseCase`
    - `StonePaymentUseCase`
    - `AbortPaymentUseCase`
- Gateway:
    - `domain/interfaces/IStoneGateway.java` (contrato)
    - `infra/StoneSdkAdapter.java` (adapter da Stone SDK)

### Stream de status de pagamento
- `PaymentEventChannelHandler` implementa `EventChannel.StreamHandler` + `IStonePaymentListener`.
- Em `onListen`: registra listener no gateway.
- Em `onCancel`: limpa `eventSink`, aborta pagamento e remove listener.

---

## 🔁 Fluxo principal

1. Flutter chama `init`
2. Android inicializa Stone SDK (`StoneStart.init`)
3. Flutter chama `activateStoneCode` com Stone Code
4. Flutter chama `payment` com `PaymentModelPlatform`
5. Status de transação é emitido via stream (`stone_plugin_stream_payment`)

---

## 🔐 Configuração de Segredos e Repositórios

### Token do repositório Stone
Em `android/settings.gradle.kts`, o token é lido por:
1. `local.properties` (`stone.token`)
2. fallback para `System.getenv("STONE_TOKEN")`

Repositórios usados:
- `google()`
- `mavenCentral()`
- `gradlePluginPortal()`
- `https://packagecloud.io/priv/$stoneToken/stone/pos-android/maven2`
- `https://storage.googleapis.com/download.flutter.io`

### Chaves para `BuildConfig`
Em `android/build.gradle.kts`, as chaves são injetadas por variáveis de ambiente:
- `QRCODE_AUTHORIZATION`
- `QRCODE_PROVIDERID`

---

## 📦 Dependências dinâmicas por hardware

No `android/build.gradle.kts`:

- `STONE_SDK_VERSION` (default: `4.15.0`)
- `POS_TYPE` (default: `sunmi`)

Vendors suportados:
- `sunmi`
- `gertec`
- `positivo`
- `tectoy`
- `ingenico`

Exemplo de build parametrizado:

```bash
./gradlew assembleRelease -PSTONE_SDK_VERSION=4.15.0 -PPOS_TYPE=sunmi
