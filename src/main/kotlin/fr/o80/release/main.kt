package fr.o80.release

fun main() {
    val output = ChangelogGenerator().generate(".changes/", "1")
    println(output)
}
