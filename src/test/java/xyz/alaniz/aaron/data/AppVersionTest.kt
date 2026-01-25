package xyz.alaniz.aaron.data

import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.test.Test

class AppVersionTest {
  @Test
  fun `version is read correctly`() {
    val fakeReader = object : ResourceReader {
      override fun open(path: String): InputStream {
        return ByteArrayInputStream("1.2.3".toByteArray())
      }
    }
    val appVersion = ResourceAppVersion(fakeReader)
    assertThat(appVersion.version).isEqualTo("1.2.3")
  }
}
