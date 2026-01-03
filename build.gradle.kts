plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
    application
}

repositories {
    mavenCentral()
    google()
}

ksp {
    arg("circuit.codegen.mode", "metro")
}

application { mainClass.set("xyz.alaniz.aaron.Main") }

version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(libs.mosaic)
    implementation(libs.circuit.foundation)
    ksp(libs.circuit.codegen)
    implementation(libs.circuit.codegen.annotations)
    runtimeOnly(compose.desktop.currentOs)
}