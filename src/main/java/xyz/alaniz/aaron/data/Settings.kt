package xyz.alaniz.aaron.data

enum class Language(val displayName: String) {
  KOTLIN("Kotlin"),
  JAVA("Java"),
  RUST("Rust"),
  PYTHON("Python"),
  JAVASCRIPT("JavaScript")
}

sealed interface CodeSnippetSettings {
  data object Disabled : CodeSnippetSettings

  data class Enabled(
      val onlyCodeSnippets: Boolean = false,
      val selectedLanguages: Set<Language> = emptySet()
  ) : CodeSnippetSettings
}

data class Settings(val codeSnippetSettings: CodeSnippetSettings = CodeSnippetSettings.Disabled)
