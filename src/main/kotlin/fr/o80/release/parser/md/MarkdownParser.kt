package fr.o80.release.parser.md

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.ParsedFileBuilder
import fr.o80.release.parser.Parser
import java.io.File

class MarkdownParser : Parser {
    override fun parse(file: File): ParsedFile {
        // TODO Plutôt que d'avoir un Builder, on peut stocker dans des variables temporaires
        //      et faire un return ParsedFile(var1, var2, etc.)
        //      l'idée est de favoriser les erreurs au compile-time plutôt qu'au runtime
        val parsedFileBuilder = ParsedFileBuilder()
        parsedFileBuilder.fileName = file.nameWithoutExtension

        file.inputStream()
            .use { inputStream ->
                MarkdownReader(inputStream)
                    .readHeaders { headers -> parsedFileBuilder.addHeaders(headers) }
                    .readLine { line -> parsedFileBuilder.title = line }
                    .readRemaining { remaining -> parsedFileBuilder.message = remaining.trim() }
            }

        return parsedFileBuilder.build()
    }
}