package fr.o80.release

data class Change(
    val id: String,
    val title: String,
    val message: String?,
    val type: String,
    val link: String?
) {
    fun toMarkdown(): String {
        val formattedId = if (link != null) "[[$id]]($link)" else "[$id]"
        return "$formattedId $title"
    }
}