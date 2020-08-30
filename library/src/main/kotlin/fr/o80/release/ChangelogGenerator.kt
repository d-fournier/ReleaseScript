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
// - pouvoir spécifier le format de sortie (autre que MD)
// - pouvoir choisir un wording différent, et pouvoir choisir comment formatter les noms de version

// TODO Unit tests ! Et les faire au fur et mesure !!! Hein Olivier !
// TODO TDD ?
class ChangelogGenerator(
    private val configuration: ChangelogConfiguration
) {

    fun generate(workingDirectory: File, versions: List<String>): String {
        val markdownParser: Parser = MarkdownParser(configuration)
        val renderer = MarkdownRenderer(configuration)
        val formatter: Formatter = TextFormatter(renderer, configuration)

        val allVersionsFolder = getVersionsFolder(workingDirectory, versions)

        allVersionsFolder.forEach { folder ->
                getChangesFiles(folder)
                    .map(markdownParser::parse)
                    .let {
                        Converter(configuration)
                            .convert(it)
                    }
                    .let { formatter.process(folder.name, it) }
            }
        return renderer.render()
    }

    private fun getVersionsFolder(workingDirectory: File, versions: List<String>): List<File> {
        return if (versions.isEmpty()) {
            workingDirectory
                .listFiles()
                ?.filter { it.isDirectory }
                ?: emptyList()
        } else {
            versions
                .map { versionName ->
                    File(workingDirectory, versionName)
                }
        }
    }

    private fun getChangesFiles(folder: File): List<File> {
        return folder
            .takeIf { it.exists() }
            ?.listFiles { file ->
                file.isFile && file.canRead()
            }
            ?.toList()
            ?: emptyList()
    }
}
