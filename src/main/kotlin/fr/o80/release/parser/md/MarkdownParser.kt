package fr.o80.release.parser.md

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.ParsedFileBuilder
import fr.o80.release.parser.Parser
import java.io.File

class MarkdownParser : Parser {
    override fun parse(file: File): ParsedFile {
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