
# Stone Smart POS Flutter Plugin 💳

[![Flutter](https://img.shields.io/badge/Flutter-v3.43+-02569B?logo=flutter&logoColor=white)](https://flutter.dev)
[![Stone SDK](https://img.shields.io/badge/Stone_SDK-v4.15.0-00A9E0?logo=android&logoColor=white)](https://docs.stone.com.br/)


Este plugin permite a integração de alto nível entre aplicações **Flutter** e o ecossistema de terminais **Smart POS** da Stone. A arquitetura foi projetada para ser reativa, thread-safe e totalmente desacoplada, facilitando a manutenção e a escalabilidade para múltiplos hardwares.

## 🛠 Desenvolvimento & Ambiente (Environment)

Para garantir a estabilidade das comunicações de baixo nível, o ambiente de desenvolvimento segue estas especificações:

### Core Stack
* **Flutter SDK:** `3.41.6` (Stable Channel)
* **Dart SDK:** `3.11.4` (Suporte avançado a Records e Pattern Matching)
* **Java JDK:** `17`
* **Gradle:** `8.7` (Kotlin DSL - KTS)

### Android Integration
* **Android Gradle Plugin (AGP):** `8.5.2`
* **Compile SDK:** `34` (Android 14)
* **Min SDK:** `23` (Android 6.0 - Requisito Stone SDK)
* **Stone SDK (Core):** `4.15.0` (Injetada dinamicamente)

---

## 🏗 Arquitetura do Sistema

A arquitetura baseia-se no padrão de **Reactive Bridge**, isolando a complexidade do hardware da interface reativa do Flutter.

### 1. Camada de Comunicação (Platform Channels)
* `MethodChannel` (**Comandos**): Chamadas assíncronas para ações imediatas (ex: `iniciarPagamento`).
* `EventChannel` (**Streams**): Fluxo de status em tempo real enviado pelo hardware (ex: `AGUARDANDO_CARTAO`, `PROCESSANDO`).



### 2. Camada Nativa (Android/Java)
* **`StonePlugin.java`**: Ponto de entrada que registra os canais.
* **`StonePluginMethodHandler.java`**: Atua como um "Roteador", desacoplando as chamadas do Flutter da lógica de hardware.
* **`StoneManager.java`**: O "Cérebro" da integração. Classe pura Java que encapsula a Stone SDK e gerencia o PINPAD de forma independente do framework.
* **`PaymentEventChannelHandler.java`**: Transmite os callbacks do hardware para a Stream do Dart.

---

## 🚀 Configuração de Build (CI/CD & Security)

O projeto utiliza **Injeção Dinâmica de Dependências** para evitar o *bloatware* (incluindo apenas o driver do fabricante necessário) e proteger credenciais sensíveis.

### Gestão de Segredos
As credenciais de acesso ao repositório privado da Stone (PackageCloud) nunca são expostas no código. Elas são recuperadas via:
1.  `gradle.properties` (Local)
2.  `System.getenv("STONE_TOKEN")` (Ambiente de CI/CD como GitHub Actions)

```kotlin
// settings.gradle.kts
val stoneToken = properties.getProperty("stone.token") ?: System.getenv("STONE_TOKEN")
```

### Multi-vendor Support
O build pode ser parametrizado para diferentes terminais (Sunmi, Gertec, Positivo, etc) via linha de comando:

```bash
./gradlew assembleRelease -PSTONE_SDK_VERSION=4.15.0 -PPOS_TYPE=sunmi
```

---

## 📦 Como Rodar o Exemplo

O repositório contém uma pasta `stone_integration_app/` que demonstra a implementação completa com **Cubit** e **Clean Architecture**.

1.  Clone o repositório.
2.  Adicione seu `stone.token` ao `gradle.properties` local.
3.  Execute `flutter pub get` na raiz e na pasta `stone_integration_app`.
4.  Conecte seu Smart POS e execute `flutter run`.

---

## 📄 Licença
Distribuído sob a licença MIT. Veja `LICENSE` para mais informações.


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
