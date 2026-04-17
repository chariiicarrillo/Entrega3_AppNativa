// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
}

// OneDrive bloquea a veces archivos temporales de Gradle y Android Studio puede
// terminar ejecutando una APK vieja. Dejamos los builds en una carpeta local
// fuera del proyecto para que Run "app" siempre genere e instale lo nuevo.
val tadeosBuildRoot = file(
    "${System.getProperty("user.home")}/.gradle/tadeos-build/${rootProject.name}"
)

layout.buildDirectory.set(tadeosBuildRoot.resolve("root"))

subprojects {
    val moduleBuildName = path
        .replace(':', '_')
        .removePrefix("_")
        .ifBlank { name }

    layout.buildDirectory.set(tadeosBuildRoot.resolve(moduleBuildName))
}
