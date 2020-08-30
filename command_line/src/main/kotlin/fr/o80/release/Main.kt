package fr.o80.release

import com.xenomachina.argparser.ArgParser
import java.io.File

fun main(args: Array<String>) {
    val parsedArgs = ArgParser(args).parseInto(::Arguments)

    val workingDirectory = File(System.getProperty("user.dir"), parsedArgs.folder)
    val configuration = ChangelogConfiguration(
        ignoreSlug = parsedArgs.ignoreSlug
    )

    val result = ChangelogGenerator(configuration).generate(workingDirectory, parsedArgs.versions)

    println(result)
}
