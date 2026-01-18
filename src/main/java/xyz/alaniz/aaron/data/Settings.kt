package xyz.alaniz.aaron.data

enum class Language(val displayName: String) {
  KOTLIN("Kotlin"),
  JAVA("Java"),
  RUST("Rust"),
  PYTHON("Python"),
  JAVASCRIPT("JavaScript")
}

data class CodeSnippetSettings(
    val enabled: Boolean = false,
    val onlyCodeSnippets: Boolean = false,
    val selectedLanguages: Set<Language> = emptySet()
)

data class Settings(val codeSnippetSettings: CodeSnippetSettings = CodeSnippetSettings())
