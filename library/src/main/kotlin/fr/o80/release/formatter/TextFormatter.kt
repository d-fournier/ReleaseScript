package fr.o80.release.formatter

import fr.o80.release.Change
import fr.o80.release.ChangelogConfiguration
import fr.o80.release.render.TextRenderer

class TextFormatter(
    private val rendererProvider: TextRenderer,
    private val configuration: ChangelogConfiguration
): Formatter {
    private var hasContent = false

    init {
        rendererProvider.header1(TITLE)
    }

    override fun process(versionName: String, changesByType: Map<String, List<Change>>) {
        hasContent = true

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
    }

    private fun Change.format(): String {
        return if (configuration.ignoreSlug) {
            title
        } else {
            val formattedId = if (link != null) ID_WITH_LINK.format(id, link) else ID.format(id)
            "$formattedId $title"
        }
    }

    companion object {
        private const val TITLE = "Changelog"
        private const val ID_WITH_LINK = "[[%s]](%s)"
        private const val ID = "[%s]"
    }
}
