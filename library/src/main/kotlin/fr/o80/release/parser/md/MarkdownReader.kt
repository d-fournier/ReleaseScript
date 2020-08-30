package fr.o80.release.parser.md

import fr.o80.release.utils.firstOrNull
import fr.o80.release.utils.reduce
import fr.o80.release.utils.takeWhile
import java.io.InputStream

class MarkdownReader(inputStream: InputStream) {

    private val splitHeaderRegex = "^([a-z-]+)\\s*:\\s*(.+)$".toRegex()

    private val streamReader = inputStream.bufferedReader()
    private val fileIterator = streamReader.lineSequence().iterator()

    fun readHeaders(): Map<String, String> {
        fileIterator.firstOrNull()
            .takeIf { it == HEADERS_SEPARATOR }
            ?: throw IllegalArgumentException("There are not headers")

        return fileIterator
            .takeWhile { it != HEADERS_SEPARATOR }
            .map {
                parseHeader(it)
            }
            .toList()
            .toMap()
    }

    fun readLine(): String? {
        return fileIterator.firstOrNull()
    }

    fun readRemaining(): String {
        return fileIterator
            .reduce { accumulator: String, line: String ->
                accumulator + "\n" + line
            }
            ?: ""
    }

    private fun parseHeader(line: String): Pair<String, String> {
        val matching = splitHeaderRegex.matchEntire(line)
            ?: throw IllegalArgumentException("A line is malformed! => $this")

        val key = matching.groupValues[1]
        val value = matching.groupValues[2]
        return key to value
    }
}

private const val HEADERS_SEPARATOR = "---"
