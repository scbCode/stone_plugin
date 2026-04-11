pluginManagement {
    val flutterSdkPath = run {
        val properties = java.util.Properties()
        file("local.properties").inputStream().use { properties.load(it) }
        val flutterSdkPath = properties.getProperty("flutter.sdk")
        require(flutterSdkPath != null) { "flutter.sdk not set in local.properties" }
        flutterSdkPath
    }
    includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
val properties = java.util.Properties()
val propertiesFile = file("local.properties")
if (propertiesFile.exists()) {
    propertiesFile.inputStream().use { properties.load(it) }
}
val sdkToken = properties.getProperty("stone.token")
// 2. Onde o Gradle busca as LIBS (Stone, Kotlin Stdlib, Flutter Engine)
dependencyResolutionManagement {
    // O modo SETTINGS orquestra o modo de resolução de dependências do Gradle
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()

        // O modo SETTINGS do Gradle precisa desse link direto
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }

        // REPOSITÓRIO DA STONE
        maven {
            url = uri("https://packagecloud.io/priv/$sdkToken/stone/pos-android/maven2")
        }
    }
}

plugins {
    id("dev.flutter.flutter-plugin-loader") version "1.0.0"
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("com.android.library") version "8.7.3" apply false
}

include(":app")