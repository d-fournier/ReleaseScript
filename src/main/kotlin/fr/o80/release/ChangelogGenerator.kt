package fr.o80.release

import fr.o80.release.parser.ParsedFile
import fr.o80.release.parser.Parser
import fr.o80.release.parser.md.MarkdownParser
import fr.o80.release.render.Renderer
import fr.o80.release.render.md.MarkdownRenderer
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence

val VALID_TYPES = arrayOf("feature", "fix")

// TODO Extraire la partie génération de markdown, avec des méthodes du genre header1, header 2, header 3 et addChange
// TODO Et aussi une couche qui s'occupe de la génération pure du markdown

// TODO Rendre la fonction principale plus paramétrable
// - pouvoir générer un changelog qui contient TOUTES les versions
// - pouvoir spécifier le format de sortie (autre que MD)
// - pouvoir choisir un wording différent, et pouvoir choisir comment formatter les noms de version

// TODO Unit tests ! Et les faire au fur et mesure !!! Hein Olivier !
// TODO TDD ?
class ChangelogGenerator {

    fun generate(workingDirectory: String, versionName: String): String {

        val markdownParser: Parser = MarkdownParser()
        val markdownRenderer: Renderer = MarkdownRenderer()

        return getChangesFiles(workingDirectory, versionName)
            .map(markdownParser::parse)
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

    private fun getChangesFiles(workingDirectory: String, versionName: String): List<File> {
        return File(workingDirectory, versionName)
            .takeIf { it.exists() }
            ?.listFiles { file ->
                file.isFile && file.canRead()
            }
            ?.toList()
            ?: emptyList()
    }
}

private fun String.isValidType(): Boolean {
    return this in VALID_TYPES
}
