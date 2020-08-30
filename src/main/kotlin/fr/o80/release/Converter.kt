package fr.o80.release

import fr.o80.release.parser.ParsedFile
import java.lang.Exception
import java.util.*

class Converter(
    private val validTypes: List<String>
) {
    fun convert(parsedFiles: List<ParsedFile>): SortedMap<String, List<Change>> {
        return parsedFiles
            .map(this::toChange)
            .groupBy { it.type }
            .toSortedMap()
    }

    private fun toChange(parsedFile: ParsedFile): Change {
        val id = parsedFile.fileName
        val title = parsedFile.title
            .takeUnless { it.isBlank() }
            .orThrow { EmptyElementException("title", parsedFile.fileName) }
        val message = parsedFile.message
            .takeIf { it.isNotBlank() }
        val type = parsedFile.headers["type"]
            .orThrow { MissingElementException("type", parsedFile.fileName) }
            .takeUnless { it.isBlank() }
            .orThrow { EmptyElementException("type", parsedFile.fileName) }
            .takeIf { it.isValidType() }
            .orThrow { InvalidArgumentException("type", parsedFile.fileName, validTypes) }

        val link = parsedFile.headers["link"]

        return Change(id, title, message, type, link)
    }

    private fun String.isValidType(): Boolean {
        return this in validTypes
    }
}

private fun <T> T?.orThrow(throwFormatter: () -> Throwable): T {
    return this ?: throw throwFormatter()
}

open class ConversionException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class MissingElementException(message: String) : ConversionException(message) {
    constructor(elementName: String, filename: String) : this("$elementName is missing from $filename")
}

class EmptyElementException(message: String) : ConversionException(message) {
    constructor(elementName: String, filename: String) : this("$elementName is empty in $filename")
}

class InvalidArgumentException(message: String) : ConversionException(message) {
    constructor(elementName: String, filename: String, expected: List<String>)
            : this("$elementName is not valid in $filename (expected=${expected.joinToString(",")}")
}
