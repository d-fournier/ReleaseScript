package fr.o80.release

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


val expectedOutputV1 = """|# Changelog
                          |
                          |## 1
                          |
                          |### Feature
                          |
                          |[[15423]](http://example.org/user-story-15423) Implement the system to do something that is really important
                          |[9875] Another feature
                          |
                          |### Fix
                          |
                          |[[1000]](http://example.org/fix-this) Nice fix
                          |""".trimMargin().replace("\n", System.lineSeparator())
val expectedOutputV2 = """|# Changelog
                          |
                          |## 2
                          |""".trimMargin().replace("\n", System.lineSeparator())

val expectedOutputAll = """|# Changelog
                           |
                           |## 2
                           |
                           |## 1
                           |
                           |### Feature
                           |
                           |[[15423]](http://example.org/user-story-15423) Implement the system to do something that is really important
                           |[9875] Another feature
                           |
                           |### Fix
                           |
                           |[[1000]](http://example.org/fix-this) Nice fix
                           |""".trimMargin().replace("\n", System.lineSeparator())

// TODO Utiliser des tests paramétrés de JUnit5
// TODO Lancer le CoCo
internal class MainKtTest {

    @Test
    fun goldenMasterTestV1() {
        val outputV1 = ChangelogGenerator().generate(".changes/", "1")
        assertEquals(expectedOutputV1, outputV1)
    }

    @Test
    fun goldenMasterTestV2() {
        val outputV2 = ChangelogGenerator().generate(".changes/", "2")
        assertEquals(expectedOutputV2, outputV2)
    }

    @Test
    fun goldenMasterTestAll() {
        val outputV1 = ChangelogGenerator().generate(".changes/", "2", "1")
        assertEquals(expectedOutputAll, outputV1)
    }
}