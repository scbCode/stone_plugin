
# Stone Integration App

[![Flutter](https://img.shields.io/badge/Flutter-v3.12+-02569B?logo=flutter&logoColor=white)](https://flutter.dev)
[![Bloc](https://img.shields.io/badge/State_Management-Bloc/Cubit-61D9FB?logo=google&logoColor=white)](https://pub.dev/packages/flutter_bloc)

Este projeto é uma demonstração de integração robusta entre uma aplicação Flutter e o SDK de pagamentos da **Stone**. Ele foi desenvolvido com foco em escalabilidade, manutenibilidade e tratamento rigoroso de estados assíncronos, simulando um cenário real de automação comercial.

## 🚀 Destaques Técnicos (Senior Level)

### 1. Arquitetura e Gerenciamento de Estado
A aplicação utiliza o padrão **BLoC (Cubit)** para separar a lógica de negócio da interface.
- **Sealed Classes para Estados:** Implementação de `PluginState` como `sealed class`, garantindo exaustividade no tratamento de estados pela UI e eliminando inconsistências.
- **Uso de Extensions:** Lógica de interface (como disponibilidade de botões baseada no estado) centralizada em extensões (`StoneStateExt`), mantendo os widgets limpos e declarativos.

### 2. Integração com Hardware e Streams
Diferente de APIs REST comuns, integrações de pagamento exigem lidar com fluxos de eventos contínuos:
- **Reactive Streams:** O processamento de pagamentos consome um `StreamSubscription` vindo do plugin nativo, permitindo atualizações de UI em tempo real (ex: "Aguardando cartão", "Processando", "Sucesso") sem polling.
- **Lifecycle Management:** Gestão cuidadosa de assinaturas de stream (`cancel()`) para evitar memory leaks e comportamentos inesperados.

### 3. Clean Code & Patterns
- **Enums Tipados:** Uso de enums para intenções de usuário (`StoneIntent`) e passos de pagamento, evitando "strings mágicas".
- **Separação de Camadas:** Divisão clara entre `UI`, `Cubit` (Business Logic) e `Data/Plugin`.
- **Tratamento de Erros:** Fluxo centralizado de captura de exceções nativas, convertendo-as em mensagens amigáveis para o usuário através do estado `PluginError`.

## 🛠 Tech Stack
- **Linguagem:** Dart
- **Framework:** Flutter
- **Gerenciamento de Estado:** flutter_bloc (Cubit)
- **Plugin Nativo:** stone_plugin (Integração customizada via MethodChannels/EventChannels)
- **Imutabilidade:** equatable

## 📱 Funcionalidades
- [x] Inicialização do SDK Stone.
- [x] Ativação de Stone Code (Terminal de pagamento).
- [x] Fluxo de pagamento completo (Crédito/Débito) com feedback em tempo real.
- [x] Impressão de comprovante via hardware integrado.
- [x] Cancelamento de operação em andamento.

## ⚙️ Como executar
Como este projeto depende de um plugin proprietário/customizado (`stone_plugin`) e hardware físico (SmartPOS), ele é destinado a demonstração de código.

1. Certifique-se de ter o Flutter instalado (`flutter doctor`).
2. Clone o repositório.
3. Certifique-se que o diretório `../stone_plugin` esteja presente ou ajuste o `pubspec.yaml`.
4. Execute `flutter pub get`.
5. Rode em um terminal SmartPOS Android compatível: `flutter run`.

---
**Nota para Recrutadores:** Este projeto reflete minha capacidade de lidar com integrações de baixo nível, hardware e fluxos de dados complexos, mantendo um código testável e organizado conforme as melhores práticas da comunidade Flutter.
