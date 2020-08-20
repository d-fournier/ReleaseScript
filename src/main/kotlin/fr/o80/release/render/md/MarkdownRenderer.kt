package fr.o80.release.render.md

import fr.o80.release.Change
import fr.o80.release.render.Renderer

class MarkdownRenderer: Renderer {
    override fun render(versionName: String, changesByType: Map<String, List<Change>>): String {
        val changelogHeader = """
                               |# Changelog
                               |
                               |## $versionName
                               |
                               |""".trimMargin()

        return changesByType
            .mapValues { (_, changes) -> changes.map { it.toMarkdown() }  }
            .map { (type, markdowns) -> toMarkdownOfType(type, markdowns) }
            .joinToString("\n\n", prefix = changelogHeader)
    }

    private fun toMarkdownOfType(type: String, markdowns: List<String>): String =
        StringBuilder()
            .append("### ")
            .append(type.capitalize())
            .append("\n\n")
            .append(markdowns.joinToString("\n"))
            .toString()
}

private fun Change.toMarkdown(): String {
    val formattedId = if (link != null) "[[$id]]($link)" else "[$id]"
    return "$formattedId $title"
}
