package fr.o80.release

import com.xenomachina.argparser.ArgParser

fun main(args: Array<String>) {
    val parsedArgs = ArgParser(args).parseInto(::Arguments)

    val result = ChangelogGenerator().generate(parsedArgs.folder, *parsedArgs.versions.toTypedArray())

    println(result)
}
