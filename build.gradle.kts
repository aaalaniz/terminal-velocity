plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    application
}

repositories {
    mavenCentral()
    google()
}

application { mainClass.set("xyz.alaniz.aaron.Main") }

version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(libs.mosaic)
    implementation(libs.circuit.foundation)
}