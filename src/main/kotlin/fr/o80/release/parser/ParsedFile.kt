package fr.o80.release.parser

data class ParsedFile(
    val fileName: String,
    val title: String,
    val message: String,
    val headers: Map<String, String> = emptyMap()
)

class ParsedFileBuilder {

    var fileName: String? = null
    var title: String? = null
    var message: String? = null
    private val headers: MutableMap<String, String> = mutableMapOf()

    fun addHeaders(headers: Map<String, String>) {
        this.headers.putAll(headers)
    }

    fun build(): ParsedFile {
        return ParsedFile(
            fileName = requireNotNull(fileName),
            title = requireNotNull(title),
            message = requireNotNull(message),
            headers = headers
        )
    }
}