package fr.o80.release.parser.md

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.Parser
import java.io.File

class MarkdownParser : Parser {
    private val splitHeaderRegex = "^([a-z-]+)\\s*:\\s*(.+)$".toRegex()

    override fun parse(file: File): ParsedFile {
        val content = file.readLines()
        val fileName = file.nameWithoutExtension

        // TODO Découper le fichier de façon séquencielle, peut-être avec un genre de builder, ou avec une API fluent
        // idée : MonObject(inputStream).readHeaders().readTitle().readMessage().toParsedFile()
        return if (content.firstOrNull() == "---") {
            val headers = content.drop(1).readHeaders()
            // FIXME Si un des header est mal formatté le "headers.size" est complètement faux
            val (title, message) = content.drop(headers.size + 2).readContent()
            ParsedFile(fileName, title, message, headers)
        } else {
            val (title, message) = content.readContent()
            ParsedFile(fileName, title, message)
        }
    }

    private fun List<String>.readHeaders(): Map<String, String> {
        return this.takeWhile { line -> line != "---" }
            .mapNotNull { line ->
                val matching = splitHeaderRegex.matchEntire(line)
                if (matching == null) {
                    null
                } else {
                    val key = matching.groupValues[1]
                    val value = matching.groupValues[2]
                    key to value
                }
            }
            .toMap()
    }

    private fun List<String>.readContent(): Pair<String, String> {
        val title = this[0]
        val message = this.dropWhile { it.isBlank() }.joinToString(System.lineSeparator())

        return title to message
    }
}