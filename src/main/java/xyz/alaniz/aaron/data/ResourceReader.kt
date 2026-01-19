package xyz.alaniz.aaron.data

import java.io.InputStream

interface ResourceReader {
  fun open(path: String): InputStream?
}
