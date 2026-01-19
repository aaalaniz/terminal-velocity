---
tags: [code, kotlin]
---
data class User(val name: String, val age: Int)

fun process(user: User) {
    println("Processing ${user.name}")
}
