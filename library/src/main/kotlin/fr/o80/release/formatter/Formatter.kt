package fr.o80.release.formatter

import fr.o80.release.Change

interface Formatter {
    fun render(versionName: String, changesByType: Map<String, List<Change>>): String
}
