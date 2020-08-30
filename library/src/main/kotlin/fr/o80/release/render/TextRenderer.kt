package fr.o80.release.render

interface TextRenderer: Renderer<String> {
    fun header1(line: String): TextRenderer
    fun header2(line: String): TextRenderer
    fun header3(line: String): TextRenderer
    fun line(line: String = ""): TextRenderer
}
