package xyz.alaniz.aaron.data

import java.io.InputStream

class ClasspathResourceReader : ResourceReader {
  override fun open(path: String): InputStream? {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
  }
}
