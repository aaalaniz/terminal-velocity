package xyz.alaniz.aaron.data

interface WordRepository {
    fun nextWord(): String
    fun getPassage(): List<String>
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

    override fun getPassage(): List<String> {
        return listOf(
            "Kotlin is a modern programming language that makes developers happier.",
            "It is concise, safe, and interoperable with Java and other languages.",
            "Mosaic is a Kotlin library for building terminal user interfaces using Jetpack Compose.",
            "Circuit is a lightweight framework for building navigation and unidirectional data flow.",
            "Metro is a compile-time dependency injection library designed for Kotlin JVM projects.",
            "By combining these technologies, we can create powerful CLI applications with ease.",
            "This typing game is a demonstration of how these libraries work together in a real-world scenario."
        )
    }
}
