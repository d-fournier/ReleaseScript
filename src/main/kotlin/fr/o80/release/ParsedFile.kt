package fr.o80.release

data class ParsedFile(
    val fileName: String,
    val title: String,
    val message: String,
    val headers: Map<String, String> = emptyMap()
)