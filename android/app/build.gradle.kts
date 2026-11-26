plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

fun readPlayBaselineVersionCode(): Int =
    rootProject.file("play_open_test_baseline_version_code.txt")
        .takeIf { it.isFile }
        ?.readText()
        ?.trim()
        ?.toIntOrNull() ?: 0

fun resolveVersionCode(): Int {
    System.getenv("VERSION_CODE")?.trim()?.toIntOrNull()?.let { return it }
    val repoRoot = rootProject.rootDir.parentFile
    val script = repoRoot.resolve("scripts/resolve_app_version_code.sh")
    if (script.isFile) {
        val proc =
            ProcessBuilder("bash", script.absolutePath)
                .directory(repoRoot)
                .redirectErrorStream(true)
                .start()
        val text = proc.inputStream.bufferedReader().readText().trim()
        proc.waitFor()
        if (proc.exitValue() == 0) {
            text.toIntOrNull()?.let { return it }
        }
    }
    val baseline = readPlayBaselineVersionCode()
    return baseline + 1
}

android {
    namespace = "dev.novux.dynasty.cbb"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dynasty.cbb"
        minSdk = 26
        targetSdk = 36
        versionCode = resolveVersionCode()
        versionName = "0.1.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            debugSymbolLevel = "SYMBOL_TABLE"
        }
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("ANDROID_KEYSTORE_PATH")?.trim().orEmpty()
            if (keystorePath.isNotEmpty()) {
                val keystoreFile = file(keystorePath)
                if (keystoreFile.exists()) {
                    storeFile = keystoreFile
                    storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                    keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                    keyPassword =
                        System.getenv("ANDROID_KEY_PASSWORD")
                            ?: System.getenv("ANDROID_KEYSTORE_PASSWORD")
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            val releaseConfig = signingConfigs.getByName("release")
            signingConfig =
                if (releaseConfig.storeFile?.exists() == true) {
                    releaseConfig
                } else {
                    signingConfigs.getByName("debug")
                }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":database"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
