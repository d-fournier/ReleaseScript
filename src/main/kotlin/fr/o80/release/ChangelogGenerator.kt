package fr.o80.release

import fr.o80.release.formatter.Formatter
import fr.o80.release.formatter.TextFormatter
import fr.o80.release.parser.Parser
import fr.o80.release.parser.md.MarkdownParser
import fr.o80.release.render.md.MarkdownRenderer
import java.io.File

// TODO Extraire la partie génération de markdown, avec des méthodes du genre header1, header 2, header 3 et addChange
// TODO Et aussi une couche qui s'occupe de la génération pure du markdown

// TODO Rendre la fonction principale plus paramétrable
// - pouvoir générer un changelog qui contient TOUTES les versions
// - pouvoir spécifier le format de sortie (autre que MD)
// - pouvoir choisir un wording différent, et pouvoir choisir comment formatter les noms de version

// TODO Unit tests ! Et les faire au fur et mesure !!! Hein Olivier !
// TODO TDD ?
class ChangelogGenerator {

    fun generate(workingDirectory: String, vararg versions: String): String {

        val markdownParser: Parser = MarkdownParser()
        val renderer = MarkdownRenderer()
        val formatter: Formatter = TextFormatter(renderer)

        versions.forEach { version ->
            getChangesFiles(workingDirectory, version)
                .map(markdownParser::parse)
                .let {
                    Converter(VALID_TYPES)
                        .convert(it)
                }
                .let { formatter.render(version, it) }
        }
        return renderer.toString()
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

private val VALID_TYPES = listOf("feature", "fix")
