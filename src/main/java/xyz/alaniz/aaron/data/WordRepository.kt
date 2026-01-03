package xyz.alaniz.aaron.data

interface WordRepository {
    fun nextWord(): String
}

class InMemoryWordRepository : WordRepository {
    private val words = listOf(
        "kotlin",
        "terminal",
        "velocity",
        "circuit",
        "mosaic",
        "metro",
        "function",
        "variable",
        "compile",
        "execute"
    )

    override fun nextWord(): String {
        return words.random()
    }
}
