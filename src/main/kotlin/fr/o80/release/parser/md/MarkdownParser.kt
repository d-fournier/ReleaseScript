package fr.o80.release.parser.md

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.Parser
import java.io.File

class MarkdownParser : Parser {
    override fun parse(file: File): ParsedFile {
        val fileName: String = file.nameWithoutExtension

        return file.inputStream()
            .use { inputStream ->
                MarkdownReader(inputStream).let {
                    val headers = it.readHeaders()
                    val title = it.readLine().orEmpty()
                    val message = it.readRemaining()
                    ParsedFile(fileName, title, message, headers)
                }
            }
    }
}