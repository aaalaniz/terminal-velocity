package xyz.alaniz.aaron.ci

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class CiPipelineTest {
  @Test
  fun `ci yaml file has correct triggers`() {
    val file = File(".github/workflows/ci.yml")
    val content = file.readText()
    assertTrue(content.contains("push:"), "Should have push trigger")
    assertTrue(content.contains("pull_request:"), "Should have pull_request trigger")
    assertTrue(content.contains("branches: [ main ]"), "Should target main branch")
  }

  @Test
  fun `ci yaml file has correct build and test steps`() {
    val file = File(".github/workflows/ci.yml")
    val content = file.readText()
    assertTrue(content.contains("actions/checkout@v4"), "Should use checkout action")
    assertTrue(content.contains("actions/setup-java@v4"), "Should use setup-java action")
    assertTrue(content.contains("java-version: '21'"), "Should use JDK 21")
    assertTrue(content.contains("./gradlew assemble"), "Should run assemble task")
    assertTrue(content.contains("./gradlew test"), "Should run test task")
  }
}
