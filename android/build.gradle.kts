plugins {
    id("com.android.library") version "8.11.1"
    id("org.jetbrains.kotlin.android") version "2.2.20" apply false
}

group = "com.scbdev.stone_plugin"
version = "1.0"

android {
    namespace = "com.scbdev.stone_plugin"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdk = 24
        // 1. Recupera a variável do ambiente (CICD ou local)
        val QRCODE_AUTHORIZATION = System.getenv("QRCODE_AUTHORIZATION") ?: "valor_default_local"
        val QRCODE_PROVIDERID = System.getenv("QRCODE_PROVIDERID") ?: "valor_default_local"

        // 2. Injeta no BuildConfig
        // O valor precisa estar entre aspas duplas ESCAPADAS para o Java entender como String
        buildConfigField("String", "QRCODE_AUTHORIZATION", "\"$QRCODE_AUTHORIZATION\"")
        buildConfigField("String", "QRCODE_PROVIDERID", "\"$QRCODE_PROVIDERID\"")
    }

    testOptions {
        unitTests.all {
            it.outputs.upToDateWhen { false }

            it.testLogging {
                events("passed", "skipped", "failed", "standardOut", "standardError")
                showStandardStreams = true
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    val stone_sdk_version =  project.findProperty("STONE_SDK_VERSION")?.toString() ?: "4.15.0"
    val posType = project.findProperty("POS_TYPE")?.toString() ?: "sunmi"

    implementation("br.com.stone:stone-sdk-posandroid:${stone_sdk_version}")

    when (posType) {
        "sunmi" -> {
            implementation("br.com.stone:stone-sdk-posandroid-sunmi:$stone_sdk_version")
        }
        "gertec" -> {
            implementation("br.com.stone:stone-sdk-posandroid-gertec:$stone_sdk_version")
        }
        "positivo" -> {
            implementation("br.com.stone:stone-sdk-posandroid-positivo:$stone_sdk_version")
        }
         "tectoy" -> {
            implementation("br.com.stone:stone-sdk-posandroid-tectoy:$stone_sdk_version")
        }
         "ingenico" -> {
            implementation("br.com.stone:stone-sdk-posandroid-ingenico:$stone_sdk_version")
        }
    }
}

