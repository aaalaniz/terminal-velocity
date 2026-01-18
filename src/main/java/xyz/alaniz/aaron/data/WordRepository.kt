package xyz.alaniz.aaron.data

import xyz.alaniz.aaron.ui.foundation.TextWrapper

interface WordRepository {
    fun getPassage(): List<String>
}

class InMemoryWordRepository(
    private val settingsRepository: SettingsRepository
) : WordRepository {

    private val prosePassages = listOf(
        """
        Kotlin is a statically typed programming language that runs on the Java Virtual Machine. It also can be compiled to JavaScript source code or use the LLVM compiler infrastructure. Its primary development is from a team of JetBrains programmers based in Saint Petersburg, Russia. While the syntax is not compatible with Java, Kotlin is designed to interoperate with Java code. It uses aggressive type inference to determine the type of values and expressions for which type has been left unstated. This makes the code more concise and easier to read compared to some other languages. Kotlin has support for functional programming, including higher-order functions and lambdas.
        """.trimIndent(),
        """
        Terminal user interfaces are a text-based user interface used to interact with a computer program. They were the dominant method of user interaction with computers during the early era of computing. Today, they are still widely used by system administrators and advanced users. Tools like Mosaic allow developers to build modern TUIs using declarative UI patterns. This brings the power of Jetpack Compose to the command line, enabling reactive UIs. With TUIs, you can create efficient and fast interfaces that run in any terminal emulator. They are particularly useful for remote server management and automation tasks.
        """.trimIndent(),
        """
        Dependency injection is a technique in which an object receives other objects that it depends on. These other objects are called dependencies, and the injection refers to the passing of a dependency. The intent behind dependency injection is to achieve separation of concerns of construction and use of objects. This can increase readability and code reuse, and make testing easier. Metro is a dependency injection framework for Kotlin that generates code at compile time. It avoids reflection, which can be slow and error-prone at runtime. By using Metro, you can ensure that your dependency graph is valid before running your application.
        """.trimIndent(),
        """
        Navigation in applications involves moving between different screens or states. In a terminal application, this might mean switching between different views or menus. Circuit is a library that provides a pattern for navigation and state management. It uses a unidirectional data flow architecture, which makes state changes predictable. Presenters in Circuit are responsible for producing the state for a given screen. The UI then renders this state, and user events are sent back to the presenter. This separation of logic and presentation makes the codebase easier to maintain.
        """.trimIndent(),
        """
        Git is a distributed version control system for tracking changes in source code during software development. It is designed for coordinating work among programmers, but it can be used to track changes in any set of files. Git was created by Linus Torvalds in 2005 for development of the Linux kernel. Every Git directory on every computer is a full-fledged repository with complete history and full version-tracking capabilities. It does not depend on network access or a central server. Common commands include git add, git commit, git push, and git pull. Branching and merging are core features that allow for parallel development.
        """.trimIndent(),
        """
        Linux is a family of open-source Unix-like operating systems based on the Linux kernel. An operating system kernel first released on September 17, 1991, by Linus Torvalds. Linux is typically packaged in a Linux distribution. Distributions include the Linux kernel and supporting system software and libraries. Many of these are provided by the GNU Project. Popular Linux distributions include Debian, Ubuntu, Fedora, Red Hat Enterprise Linux, and Arch Linux. Linux is the leading operating system on servers and other big iron systems.
        """.trimIndent(),
        """
        The Java Virtual Machine (JVM) is a virtual machine that enables a computer to run Java programs. It also runs programs written in other languages that are also compiled to Java bytecode. The JVM is detailed by a specification that formally describes what is required of a JVM implementation. Having a specification ensures interoperability of Java programs across different implementations. Key features include memory management via garbage collection and just-in-time compilation. The JVM allows Java programs to follow the write once, run anywhere principle. It provides a platform-independent execution environment that converts bytecode into machine code.
        """.trimIndent(),
        """
        Object-oriented programming (OOP) is a programming paradigm based on the concept of objects. Objects can contain data, in the form of fields, and code, in the form of procedures. A feature of objects is that an object's procedures can access and often modify the data fields of the object. In OOP, computer programs are designed by making them out of objects that interact with one another. The most popular OOP languages are class-based, meaning that objects are instances of classes. Key concepts in OOP include encapsulation, inheritance, and polymorphism. These concepts help in organizing complex software systems and promoting code reuse.
        """.trimIndent(),
        """
        Functional programming is a programming paradigm where programs are constructed by applying and composing functions. It is a declarative programming paradigm in which function definitions are trees of expressions that map values to other values. Functional programming avoids changing-state and mutable data. It emphasizes the application of functions, in contrast to the imperative programming style. Key concepts include pure functions, higher-order functions, and immutability. Languages like Haskell, Lisp, and Erlang are primarily functional. Modern languages like Kotlin and Swift also support functional programming features.
        """.trimIndent(),
        """
        A compiler is a computer program that translates computer code written in one programming language into another language. The name compiler is primarily used for programs that translate source code from a high-level programming language to a lower level language. This lower level language is typically assembly language, object code, or machine code. The most common reason for converting source code is to create an executable program. Compilers perform many operations, including lexical analysis, preprocessing, parsing, and semantic analysis. They also perform code generation and code optimization. A compiler is distinct from an interpreter, which executes the program directly.
        """.trimIndent(),
        """
        An algorithm is a finite sequence of well-defined, computer-implementable instructions. They are typically used to solve a class of problems or to perform a computation. Algorithms are always unambiguous and are used as specifications for performing calculations and data processing. They can be expressed in many kinds of notation, including natural language, pseudocode, and flowcharts. Analysis of algorithms is a major discipline in computer science. It involves finding the resources, such as time and storage, needed to execute them. Big O notation is often used to describe the limiting behavior of a function.
        """.trimIndent(),
        """
        Data structures are a way of organizing and storing data so that they can be accessed and worked with efficiently. They define the relationship between the data, and the operations that can be performed on the data. There are many different types of data structures, each suited to different kinds of applications. Common data structures include arrays, linked lists, stacks, queues, and trees. Hash tables are used for fast data retrieval based on keys. Graphs are used to represent networks and relationships between objects. Choosing the right data structure is crucial for writing efficient algorithms.
        """.trimIndent(),
        """
        Concurrent programming is a form of computing in which several computations are executed during overlapping time periods. This is in contrast to sequential computing, where one is executed after another. Concurrency allows a program to make progress on multiple tasks simultaneously. It is often used to improve the performance of applications on multi-core processors. Threads are the smallest unit of processing that can be scheduled by an operating system. Synchronization mechanisms like locks and semaphores are used to coordinate access to shared resources. Deadlocks and race conditions are common problems in concurrent programming.
        """.trimIndent(),
        """
        A database is an organized collection of data, generally stored and accessed electronically from a computer system. The database management system (DBMS) is the software that interacts with end users, applications, and the database itself. It allows for the definition, creation, querying, update, and administration of databases. Structured Query Language (SQL) is a domain-specific language used in programming and designed for managing data held in a relational database management system. NoSQL databases provide a mechanism for storage and retrieval of data that is modeled in means other than the tabular relations used in relational databases. Databases are essential for storing and retrieving large amounts of information. They ensure data integrity, security, and availability.
        """.trimIndent(),
        """
        Cloud computing is the on-demand availability of computer system resources, especially data storage and computing power. It does not require direct active management by the user. The term is generally used to describe data centers available to many users over the Internet. Large clouds, predominant today, often have functions distributed over multiple locations from central servers. Cloud computing relies on sharing of resources to achieve coherence and economies of scale. Services are often categorized as Infrastructure as a Service (IaaS), Platform as a Service (PaaS), and Software as a Service (SaaS). Amazon Web Services, Microsoft Azure, and Google Cloud Platform are major providers.
        """.trimIndent(),
        """
        Cybersecurity is the practice of protecting systems, networks, and programs from digital attacks. These cyberattacks are usually aimed at accessing, changing, or destroying sensitive information. They can also be used to extort money from users or interrupt normal business processes. Implementing effective cybersecurity measures is particularly challenging today because there are more devices than people. Attackers are becoming more innovative. Key elements include network security, application security, and information security. Regular updates and patches are crucial for maintaining security.
        """.trimIndent(),
        """
        Artificial intelligence (AI) is intelligence demonstrated by machines, as opposed to the natural intelligence displayed by animals including humans. AI research has been defined as the field of study of intelligent agents. An intelligent agent is any system that perceives its environment and takes actions that maximize its chance of achieving its goals. Machine learning is a subset of AI that involves the study of computer algorithms that improve automatically through experience. Deep learning is a class of machine learning algorithms that uses multiple layers to progressively extract higher-level features from the raw input. AI applications include web search engines, recommendation systems, and autonomous vehicles. Ethical considerations regarding AI are a growing topic of discussion.
        """.trimIndent(),
        """
        Software engineering is the systematic application of engineering approaches to the development of software. It involves the design, development, maintenance, testing, and evaluation of computer software. Software engineers apply the principles of engineering to the software development process. The software development life cycle (SDLC) is a process used by the software industry to design, develop and test high quality software. Common models include the Waterfall model, Agile model, and Spiral model. Requirements engineering is the process of defining, documenting, and maintaining requirements. Testing ensures that the software meets the requirements and is free of bugs.
        """.trimIndent(),
        """
        A web browser is a software application for accessing information on the World Wide Web. When a user requests a web page from a particular website, the web browser retrieves the necessary content from a web server. It then displays the page on the user's device. Browsers interpret HTML, CSS, and JavaScript to render web pages. The first web browser, called WorldWideWeb, was created in 1990 by Sir Tim Berners-Lee. Modern browsers include Google Chrome, Mozilla Firefox, Apple Safari, and Microsoft Edge. They support features like tabs, bookmarks, and extensions.
        """.trimIndent(),
        """
        Mobile app development is the act or process by which a mobile app is developed for mobile devices. These applications can be pre-installed on phones during manufacturing platforms. They can also be delivered as web applications using server-side or client-side processing. Android and iOS are the two dominant mobile operating systems. Native development involves writing apps specifically for one platform using languages like Kotlin or Swift. Cross-platform development allows developers to write code once and deploy it to multiple platforms. Frameworks like React Native, Flutter, and Xamarin are popular for cross-platform development.
        """.trimIndent()
    )

    private val codePassages = mapOf(
        Language.KOTLIN to listOf(
            """
            fun main() {
                println("Hello, World!")
            }
            """.trimIndent(),
            """
            data class User(val name: String, val age: Int)

            fun process(user: User) {
                println("Processing ${'$'}{user.name}")
            }
            """.trimIndent()
        ),
        Language.JAVA to listOf(
             """
             public class HelloWorld {
                 public static void main(String[] args) {
                     System.out.println("Hello, World!");
                 }
             }
             """.trimIndent(),
             """
             import java.util.List;
             import java.util.ArrayList;

             public class Example {
                 public void demo() {
                     List<String> list = new ArrayList<>();
                     list.add("Item 1");
                 }
             }
             """.trimIndent()
        ),
        Language.RUST to listOf(
            """
            fn main() {
                println!("Hello, world!");
            }
            """.trimIndent(),
            """
            struct User {
                username: String,
                email: String,
                sign_in_count: u64,
                active: bool,
            }
            """.trimIndent()
        ),
        Language.PYTHON to listOf(
            """
            def greet(name):
                print(f"Hello, {name}!")

            if __name__ == "__main__":
                greet("World")
            """.trimIndent(),
            """
            class Dog:
                def __init__(self, name):
                    self.name = name

                def bark(self):
                    return "Woof!"
            """.trimIndent()
        ),
        Language.JAVASCRIPT to listOf(
            """
            function greet(name) {
                console.log(`Hello, ${'$'}{name}!`);
            }

            greet('World');
            """.trimIndent(),
            """
            const user = {
                name: 'Alice',
                age: 30,
                isAdmin: true
            };
            """.trimIndent()
        )
    )

    override fun getPassage(): List<String> {
        val settings = settingsRepository.settings.value
        val availablePassages = mutableListOf<String>()

        availablePassages.addAll(prosePassages)

        if (settings.codeSnippetSettings.enabled) {
             settings.codeSnippetSettings.selectedLanguages.forEach { language ->
                 codePassages[language]?.let { availablePassages.addAll(it) }
             }
        }

        if (availablePassages.isEmpty()) return emptyList()

        val rawPassage = availablePassages.random()
        return TextWrapper.wrap(rawPassage)
    }
}
