package fr.o80.release.formatter

import fr.o80.release.Change
import fr.o80.release.render.TextRenderer

class TextFormatter(
    private val rendererProvider: TextRenderer
): Formatter {
    init {
        rendererProvider.header1("Changelog")
    }

    override fun render(versionName: String, changesByType: Map<String, List<Change>>): String {
        rendererProvider
            .line()
            .header2(versionName)

        changesByType
            .forEach { (type, formattedStr) ->
                rendererProvider
                    .line()
                    .header3(type.capitalize())
                    .line()
                    .apply {
                        formattedStr.forEach {
                            line(it.format())
                        }
                    }
            }
        return rendererProvider.toString()
    }
}

private fun Change.format(): String {
    val formattedId = if (link != null) "[[$id]]($link)" else "[$id]"
    return "$formattedId $title"
}
