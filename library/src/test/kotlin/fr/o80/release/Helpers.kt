package fr.o80.release

import java.io.File

object Helpers {
    fun getFolder(subFolder: String): File {
        return File(this::class.java.getResource("/$subFolder").toURI())
    }

    fun getFile(filePath: String): File = File(this::class.java.getResource("/$filePath").toURI())

    val configuration = ChangelogConfiguration(
        ignoreSlug = false
    )
}
