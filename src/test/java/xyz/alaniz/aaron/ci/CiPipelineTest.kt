package xyz.alaniz.aaron.ci

import com.google.common.truth.Truth.assertThat
import java.io.File
import org.junit.jupiter.api.Test

class CiPipelineTest {
  @Test
  fun `ci yaml file has correct triggers`() {
    val file = File(".github/workflows/ci.yml")
    val content = file.readText()
    assertThat(content).contains("push:")
    assertThat(content).contains("pull_request:")
    assertThat(content).contains("branches: [ main ]")
  }

  @Test
  fun `ci yaml file has correct build and test steps`() {
    val file = File(".github/workflows/ci.yml")
    val content = file.readText()
    assertThat(content).contains("actions/checkout@v4")
    assertThat(content).contains("actions/setup-java@v4")
    assertThat(content).contains("java-version: '21'")
    assertThat(content).contains("./gradlew assemble")
    assertThat(content).contains("./gradlew test")
  }
}
