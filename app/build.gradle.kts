plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tadeos"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.tadeos"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

// Android Studio puede conservar salidas incrementales antiguas cuando el
// proyecto vive en OneDrive. Para que Run "app" no vuelva a instalar pantallas
// viejas, cada build debug empieza borrando las salidas anteriores del modulo.
val purgeDebugBuildCache by tasks.registering(Delete::class) {
    delete(layout.buildDirectory)
}

// Tambien limpiamos la ruta clasica app/build por si Android Studio la revisa.
val legacyDebugApk = layout.projectDirectory.file("build/outputs/apk/debug/app-debug.apk")

val deleteLegacyDebugApk by tasks.registering(Delete::class) {
    delete(legacyDebugApk)
}

val mirrorDebugApkToLegacyBuild by tasks.registering(Copy::class) {
    from(layout.buildDirectory.file("outputs/apk/debug/app-debug.apk"))
    into(layout.projectDirectory.dir("build/outputs/apk/debug"))
}

tasks.matching { it.name == "preDebugBuild" }.configureEach {
    dependsOn(purgeDebugBuildCache, deleteLegacyDebugApk)
}

tasks.matching { it.name == "packageDebug" }.configureEach {
    finalizedBy(mirrorDebugApkToLegacyBuild)
}

tasks.matching { it.name == "assembleDebug" }.configureEach {
    finalizedBy(mirrorDebugApkToLegacyBuild)
}
