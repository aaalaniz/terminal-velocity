package xyz.alaniz.aaron.data

interface WordRepository {
  suspend fun getPassage(): List<String>
}
