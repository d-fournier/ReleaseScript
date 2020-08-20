package fr.o80.release.parser.md

import java.io.InputStream

const val HEADERS_SEPARATOR = "---"

class MarkdownReader(inputStream: InputStream) {

    private val splitHeaderRegex = "^([a-z-]+)\\s*:\\s*(.+)$".toRegex()

    // TODO Voir le Reader comme une séquence avec des méthodes .map .take .filter
    private val streamReader = inputStream.bufferedReader()

    fun readHeaders(block: (Map<String, String>) -> Unit): MarkdownReader {
        streamReader.readLine().takeIf { it == HEADERS_SEPARATOR }
            ?: throw IllegalArgumentException("There are not headers")
        val headers = mutableMapOf<String, String>()

        var line = streamReader.readLine()
        while (line != HEADERS_SEPARATOR) {

            val (key, value) = parseHeader(line)
            headers[key] = value
            line = streamReader.readLine()
        }

        block(headers)

        return this
    }

    // TODO readLine peut renvoyer un null (ce crash peut arriver à d'autres endroits du fichier)
    fun readLine(block: (String) -> Unit): MarkdownReader {
        block(streamReader.readLine())
        return this
    }

    fun readRemaining(block: (String) -> Unit): MarkdownReader {
        val builder = StringBuilder()
        var line = streamReader.readLine()
        while (line != null) {
            builder.append(line).append("\n")
            line = streamReader.readLine()
        }
        block(builder.removeSuffix("\n").toString())
        return this
    }

    private fun parseHeader(line: String): Pair<String, String> {
        val matching = splitHeaderRegex.matchEntire(line)
            ?: throw IllegalArgumentException("A line is malformed! => $this")

        val key = matching.groupValues[1]
        val value = matching.groupValues[2]
        return key to value
    }
}

