plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.metro)
  alias(libs.plugins.ksp)
  alias(libs.plugins.ktfmt)
  application
}

repositories {
  mavenCentral()
  google()
}

ksp { arg("circuit.codegen.mode", "metro") }

application {
  mainClass.set("xyz.alaniz.aaron.Main")
  applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

version = "1.0.0-SNAPSHOT"

dependencies {
  implementation(libs.mosaic)
  implementation(libs.circuit.foundation)
  ksp(libs.circuit.codegen)
  implementation(libs.circuit.codegen.annotations)
  runtimeOnly(compose.desktop.currentOs)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.circuit.test)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.mosaic.testing)
}

tasks.withType<Test> { useJUnitPlatform() }
