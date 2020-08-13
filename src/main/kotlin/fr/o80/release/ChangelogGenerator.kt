package fr.o80.release

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.md.MarkdownParser
import fr.o80.release.render.md.MarkdownRenderer
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

    fun generate(workingDirectory: String, versionName: String): String {

        val markdownRenderer = MarkdownRenderer()

        return getChangesFiles(workingDirectory, versionName)
            .map(this::parseFile)
            .mapNotNull(this::toChange)
            .groupBy { it.type }
            .toSortedMap()
            .let { markdownRenderer.render(versionName, it) }
    }

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
        val markdownParser = MarkdownParser()
        val file = path.toFile()
        return markdownParser.parse(file)
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
