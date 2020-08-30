package fr.o80.release.formatter

import fr.o80.release.Change

interface Formatter {
    fun process(versionName: String, changesByType: Map<String, List<Change>>)
}
