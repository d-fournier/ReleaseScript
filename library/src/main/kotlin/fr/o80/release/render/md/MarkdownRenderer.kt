package fr.o80.release.render.md

import fr.o80.release.render.TextRenderer

class MarkdownRenderer : TextRenderer {
    private val builder = StringBuilder()

    override fun header1(line: String): TextRenderer {
        builder.appendln(HEADER_1.format(line))
        return this
    }

    override fun header2(line: String): TextRenderer {
        builder.appendln(HEADER_2.format(line))
        return this
    }

    override fun header3(line: String): TextRenderer {
        builder.appendln(HEADER_3.format(line))
        return this
    }

    override fun line(line: String): TextRenderer {
        builder.appendln(line)
        return this
    }

    override fun toString(): String {
        return builder.toString()
    }
}

private const val HEADER_1 = "# %s"
private const val HEADER_2 = "## %s"
private const val HEADER_3 = "### %s"
