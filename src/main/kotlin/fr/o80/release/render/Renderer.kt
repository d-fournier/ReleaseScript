package fr.o80.release.render

import fr.o80.release.Change

interface Renderer {
    fun render(versionName: String, changesByType: Map<String, List<Change>>): String
}
