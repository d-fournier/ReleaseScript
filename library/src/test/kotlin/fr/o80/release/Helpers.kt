package fr.o80.release

import java.io.File

object Helpers {
    fun getFolderPath(subFolder: String): String {
        return File(this::class.java.getResource("/$subFolder").toURI()).toString()
    }

    fun getFile(filePath: String): File = File(this::class.java.getResource("/$filePath").toURI())
}