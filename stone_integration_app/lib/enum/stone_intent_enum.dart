enum StoneIntent { initialize, print, activateStonecode, amountSelector,  cancel, none }

extension StoneIntentText on StoneIntent {
  String get customName {
    return switch (this) {
      StoneIntent.initialize => '🚀 Inicializar',
      StoneIntent.print => '📄 Imprimir Teste',
      StoneIntent.activateStonecode => '🔑 Ativa Stonecode',
      StoneIntent.amountSelector => '💰 Selecionar valor',
      StoneIntent.cancel => '❌ Cancelar',
      StoneIntent.none => '---',
    };
  }
}
