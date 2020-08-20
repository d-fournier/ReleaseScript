package fr.o80.release.parser

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ParsedFileBuilderTest {

    private val builder = ParsedFileBuilder()

    @Test
    fun `Should build a ParsedFile`() {
        // Arrange

        // Act
        builder.title = "titre"
        builder.addHeaders(mapOf("type" to "fix"))
        builder.fileName = "1054.md"
        builder.message = "Le message"
        val output = builder.build()

        // Assert
        assertThat(output.title).isEqualTo("titre")
        assertThat(output.headers).containsExactly("type", "fix")
        assertThat(output.fileName).isEqualTo("1054.md")
        assertThat(output.message).isEqualTo("Le message")
    }
}