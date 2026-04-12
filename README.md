# Stone Smart POS Flutter Plugin 💳

[![Flutter](https://img.shields.io/badge/Flutter-Plugin-02569B?logo=flutter&logoColor=white)](https://flutter.dev)
[![Dart](https://img.shields.io/badge/Dart-3.11.4-0175C2?logo=dart&logoColor=white)](https://dart.dev)
[![Android](https://img.shields.io/badge/Android-AGP%208.7.3-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Stone SDK](https://img.shields.io/badge/Stone_SDK-4.15.0+-00A9E0)](https://sdkandroid.stone.com.br/docs/o-que-e-a-sdk-android)

Plugin Flutter para integração com terminais **Smart POS** da Stone, combinando comunicação reativa (`MethodChannel` + `EventChannel`) com uma arquitetura desacoplada para facilitar evolução, manutenção e suporte a múltiplos hardwares.

---

## ✨ Destaques

- Ponte Flutter ↔ Android nativo para operações de pagamento
- Fluxo assíncrono com atualização de status em tempo real
- Camadas organizadas por responsabilidade (`UseCases`, `Gateway`, `Adapter`)
- Configuração dinâmica por fabricante de terminal POS
- Estratégia de configuração segura para segredos em ambiente local e CI/CD

## 🎬 Demonstração

![Demonstração do fluxo de pagamento no Smart POS](docs/stone-plugin-demo.gif)

---

## 🏗 Arquitetura

### Camada Flutter (Dart)
- `lib/stone_plugin.dart`: API pública do plugin
- `lib/stone_plugin_method_channel.dart`: implementação via platform channels
- `lib/stone_plugin_platform_interface.dart`: contrato de plataforma com `PlatformInterface.verifyToken(instance, _token)`

### Camada Android (Java)
- `android/src/main/java/.../StonePlugin.java`
    - Entry point do plugin
    - Implementa `FlutterPlugin`, `MethodCallHandler`, `ActivityAware`
    - Registra:
        - `MethodChannel("stone_plugin")`
        - `EventChannel("stone_plugin_stream_payment")`
- `android/src/main/java/.../domain/usecases/`
    - `StoneInitUseCase`
    - `ActivateStonecodeUseCase`
    - `StonePaymentUseCase`
    - `AbortPaymentUseCase`
- `android/src/main/java/.../domain/interfaces/IStoneGateway.java`: contrato da camada de domínio
- `android/src/main/java/.../infra/StoneSdkAdapter.java`: integração com Stone SDK
- `android/src/main/java/.../channel/PaymentEventChannelHandler.java`: stream de eventos para o Flutter

---

## 🔁 Fluxo principal

1. Flutter chama `init`
2. Android inicializa a Stone SDK (`StoneStart.init`)
3. Flutter chama `activateStoneCode` com o Stone Code
4. Flutter chama `payment` com `PaymentModelPlatform`
5. O status da transação é emitido via stream `stone_plugin_stream_payment`

---

## 🛠 Stack atual

### Core
- Dart SDK: `^3.11.4`
- Java: `17`
- Gradle Wrapper: `8.9`

### Android
- Android Gradle Plugin (AGP): `8.7.3`
- Kotlin Gradle Plugin: `2.0.21`
- `compileSdk`: `36`
- `minSdk`: `24`
- Stone SDK (default): `4.15.0` (parametrizável)

---

## 🔐 Configuração e segurança

### Token do repositório Stone
Em `android/settings.gradle.kts`, o token é resolvido por:
1. `local.properties` (`stone.token`)
2. fallback para variável de ambiente `STONE_TOKEN`

Repositórios utilizados:
- `google()`
- `mavenCentral()`
- `gradlePluginPortal()`
- `https://packagecloud.io/priv/$stoneToken/stone/pos-android/maven2`
- `https://storage.googleapis.com/download.flutter.io`

### Campos no `BuildConfig`
Em `android/build.gradle.kts`, via variáveis de ambiente:
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

Exemplo de build:

./gradlew assembleRelease -PSTONE_SDK_VERSION=4.15.0 -PPOS_TYPE=sunmi

## 📚 Referências e Documentação Consultada

Para o desenvolvimento deste plugin e a implementação da arquitetura de integração, foram consultadas as seguintes fontes oficiais:

### Core Framework (Flutter & Dart)
**[MethodCall Javadoc (io.flutter.plugin.common):](https://api.flutter.dev/javadoc/io/flutter/plugin/common/MethodCall.html)** Documentação técnica da engine do Flutter para o tratamento de chamadas recebidas do Dart no lado nativo (Android).

**[Flutter Platform Channels:](https://docs.flutter.dev/platform-integration/platform-channels)** Guia oficial sobre a comunicação bidirecional entre Dart e código nativo via MethodChannel e EventChannel.

**[Dart 3.0 - Records & Patterns:](https://dart.dev/language/records)** Estruturas de dados modernas utilizadas para retornos múltiplos e tipados no projeto.

### Hardware & SDK (Stone)

**[Stone SDK Android - Documentação Oficial:](https://sdkandroid.stone.com.br/docs/o-que-e-a-sdk-android)** Portal do desenvolvedor Stone com as especificações de integração para Smart POS.

### Build & Automação (Gradle)
**[Gradle Kotlin DSL (KTS) Primer:](https://docs.gradle.org/current/userguide/kotlin_dsl.html)** Documentação sobre o gerenciamento de builds Type-Safe utilizando Kotlin em vez de Groovy.


## 👨‍💻 Autor

**Saulo Costa Barbosa** *Senior Mobile Software Engineer & Flutter Specialist*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/saulo-barbosa-07647a195/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/scbCode)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:saulo.cbarbosa@gmail.com)
