package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.InputStream

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ClasspathResourceReader : ResourceReader {
  override fun open(path: String): InputStream {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        ?: throw IllegalArgumentException("Resource not found: $path")
  }
}
