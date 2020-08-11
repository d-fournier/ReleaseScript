package fr.o80.release

import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence

val VALID_TYPES = arrayOf("feature", "fix")

// TODO Extraire la partie génération de markdown, avec des méthodes du genre header1, header 2, header 3 et addChange
// TODO Et aussi une couche qui s'occupe de la génération pure du markdown
// TODO Remplacer l'API Path, par l'API File(s)

// TODO Rendre la fonction principale plus paramétrable
// - pouvoir générer un changelog qui contient TOUTES les versions
// - pouvoir spécifier le format de sortie (autre que MD)
// - pouvoir choisir un wording différent, et pouvoir choisir comment formatter les noms de version

// TODO Unit tests ! Et les faire au fur et mesure !!! Hein Olivier !
// TODO TDD ?
class ChangelogGenerator {

    private val splitHeaderRegex = "^([a-z-]+)\\s*:\\s*(.+)$".toRegex()

    fun generate(workingDirectory: String, versionName: String): String {
        val changelogHeader = """
                               |# Changelog
                               |
                               |## $versionName
                               |
                               |""".trimMargin()

        // TODO Sortir l'algo de parsing du fichier dans une autre classe, qui reçoit des File et qui donne des Change
        return getChangesFiles(workingDirectory, versionName)
            .map(this::parseFile)
            .mapNotNull(this::toChange)
            .groupBy { it.type }
            .toSortedMap()
            .mapValues { (_, changes) -> changes.map(Change::toMarkdown) }
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

    private fun toChange(parsedFile: ParsedFile): Change? {
        val id = parsedFile.fileName
        val title = parsedFile.title.takeUnless { it.isBlank() } ?: return null
        val message = parsedFile.message.takeIf { it.isNotBlank() }
        val type =
            parsedFile.headers["type"].takeUnless { it.isNullOrBlank() }?.takeIf { it.isValidType() } ?: return null
        val link = parsedFile.headers["link"]

        return Change(id, title, message, type, link)
    }

    private fun parseFile(path: Path): ParsedFile {
        val file = path.toFile()
        val content = file.readLines()
        val fileName = file.nameWithoutExtension

        // TODO Découper le fichier de façon séquencielle, peut-être avec un genre de builder, ou avec une API fluent
        // idée : MonObject(inputStream).readHeaders().readTitle().readMessage().toParsedFile()
        return if (content.firstOrNull() == "---") {
            val headers = content.drop(1).readHeaders()
            // FIXME Si un des header est mal formatté le "headers.size" est complètement faux
            val (title, message) = content.drop(headers.size + 2).readContent()
            ParsedFile(fileName, title, message, headers)
        } else {
            val (title, message) = content.readContent()
            ParsedFile(fileName, title, message)
        }
    }

    private fun List<String>.readHeaders(): Map<String, String> {
        return this.takeWhile { line -> line != "---" }
            .mapNotNull { line ->
                val matching = splitHeaderRegex.matchEntire(line)
                if (matching == null) {
                    null
                } else {
                    val key = matching.groupValues[1]
                    val value = matching.groupValues[2]
                    key to value
                }
            }
            .toMap()
    }

    private fun List<String>.readContent(): Pair<String, String> {
        val title = this[0]
        val message = this.dropWhile { it.isBlank() }.joinToString(System.lineSeparator())

        return title to message
    }

    private fun getChangesFiles(workingDirectory: String, versionName: String): Sequence<Path> {
        val workingPath = Path.of(workingDirectory, versionName)
        return Files.list(workingPath)
            .filter { path -> Files.isRegularFile(path) && Files.isReadable(path) }
            .asSequence()
    }

}

private fun String.isValidType(): Boolean {
    return this in VALID_TYPES
}
