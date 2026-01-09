package xyz.alaniz.aaron.data

val codeSnippets = mapOf(
    Language.KOTLIN to listOf(
        """
        fun main() {
            println("Hello, World!")
            val numbers = listOf(1, 2, 3, 4, 5)
            val doubled = numbers.map { it * 2 }
            println(doubled)
        }
        """.trimIndent(),
        """
        data class User(val name: String, val age: Int)

        fun getUser(id: Int): User? {
            return if (id == 1) User("Alice", 30) else null
        }
        """.trimIndent()
    ),
    Language.JAVA to listOf(
        """
        public class HelloWorld {
            public static void main(String[] args) {
                System.out.println("Hello, World!");
                for (int i = 0; i < 5; i++) {
                    System.out.println("Count: " + i);
                }
            }
        }
        """.trimIndent(),
        """
        import java.util.ArrayList;
        import java.util.List;

        public class ListExample {
            public void demo() {
                List<String> items = new ArrayList<>();
                items.add("Apple");
                items.add("Banana");
                items.forEach(System.out::println);
            }
        }
        """.trimIndent()
    ),
    Language.RUST to listOf(
        """
        fn main() {
            println!("Hello, world!");
            let x = 5;
            let y = 10;
            println!("x = {}, y = {}", x, y);
        }
        """.trimIndent(),
        """
        struct Point {
            x: i32,
            y: i32,
        }

        fn main() {
            let origin = Point { x: 0, y: 0 };
            println!("The origin is at ({}, {})", origin.x, origin.y);
        }
        """.trimIndent()
    ),
    Language.PYTHON to listOf(
        """
        def greet(name):
            return f"Hello, {name}!"

        if __name__ == "__main__":
            print(greet("World"))
            numbers = [1, 2, 3, 4, 5]
            squares = [n**2 for n in numbers]
            print(squares)
        """.trimIndent(),
        """
        class Dog:
            def __init__(self, name):
                self.name = name

            def bark(self):
                return "Woof!"

        dog = Dog("Buddy")
        print(dog.name + " says " + dog.bark())
        """.trimIndent()
    ),
    Language.JAVASCRIPT to listOf(
        """
        function greet(name) {
            console.log(`Hello, ${'$'}{name}!`);
        }

        const numbers = [1, 2, 3, 4, 5];
        const doubled = numbers.map(n => n * 2);
        console.log(doubled);
        greet("World");
        """.trimIndent(),
        """
        class Car {
            constructor(brand) {
                this.brand = brand;
            }

            display() {
                return "I have a " + this.brand;
            }
        }

        const myCar = new Car("Ford");
        console.log(myCar.display());
        """.trimIndent()
    )
)
