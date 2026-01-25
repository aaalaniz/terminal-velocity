package xyz.alaniz.aaron.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

interface AppVersion {
  val version: String
}

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ResourceAppVersion(
    private val resourceReader: ResourceReader,
) : AppVersion {
  override val version: String by lazy {
    resourceReader.open("VERSION").bufferedReader().use { it.readText().trim() }
  }
}
