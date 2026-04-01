# regras de minificação/obfuscação para integração com SDK Stone
# Link: https://sdkandroid.stone.com.br/page/artigo-exce%C3%A7%C3%B5es-proguard-ap%C3%AAndice

-keep class br.com.stone.** {*;}
-keep class stone.** {*;}
-keep class com.thoughtworks.xstream.mapper.EnumMapper {*;}
-keep class com.thoughtworks.xstream.mapper.AnnotationMapper {*;}
-keep class com.thoughtworks.xstream.converters.** {*;}

-keep class com.scbdev.stone_plugin.BuildConfig { *; }
-keep class com.scbdev.stone_plugin.StoneManager { *; }