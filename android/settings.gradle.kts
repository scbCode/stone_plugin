
val properties = java.util.Properties()
val propertiesFile = file("local.properties")
if (propertiesFile.exists()) {
    propertiesFile.inputStream().use { properties.load(it) }
}
// 1. Recuperação Segura de Credenciais (Stone SDK Access Token)
// Prioriza o arquivo local 'local.properties' para desenvolvimento,
// com fallback para 'System.getenv' em ambientes de CI/CD (GitHub Actions/Codemagic).
val stoneToken = properties.getProperty("stone.token") ?: System.getenv("STONE_TOKEN")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

    dependencyResolutionManagement {

        // 2. Centralização da Gestão de Repositórios
        // 'PREFER_SETTINGS' garante que todas as dependências do projeto e módulos
        // sigam as mesmas fontes de download definidas aqui.
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
            google()
            mavenCentral()

            // 3. Repositório Privado da Stone (PackageCloud)
            // Utiliza o token autenticado para acessar os artefatos fechados da SDK.
            // A interpolação dinâmica evita o 'hardcoding' de segredos no controle de versão.
            maven {
                url = uri("https://packagecloud.io/priv/$stoneToken/stone/pos-android/maven2")
            }
            maven {
                url = uri("https://storage.googleapis.com/download.flutter.io")
            }
        }
    }

    rootProject.name = "stone_plugin"