package fr.o80.release.parser.md

import com.google.common.truth.Truth.assertThat
import fr.o80.release.Helpers
import fr.o80.release.Helpers.getFile
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


internal class MarkdownParserTest {

    private val markdownParser = MarkdownParser(Helpers.configuration)

    @Test
    @DisplayName("Parse a file with headers and title only")
    fun `Should parse a file with headers and title only`() {
        // Arrange
        val inputFile = getFile(".changes_test/1/1000.md")

        // Act
        val output = markdownParser.parse(inputFile)

        // Assert
        assertThat(output.title).isEqualTo("Nice fix")
        assertThat(output.headers).run {
            hasSize(2)
            containsEntry("type", "fix")
            containsEntry("link", "http://example.org/fix-this")
        }
    }

    @Test
    @DisplayName("Parse a file with empty headers and title")
    fun `Should parse a file with empty headers and title`() {
        // Arrange
        val inputFile = getFile(".changes_test/1/1500.md")

        // Act
        val output = markdownParser.parse(inputFile)

        // Assert
        assertThat(output.title).isEqualTo("There's an empty headers")
        assertThat(output.headers).run {
            hasSize(0)
        }
    }

    @Test
    @DisplayName("Parse a file without headers and with title")
    fun `Should parse a file without headers and with title`() {
        // Arrange
        val inputFile = getFile(".changes_test/1/1520.md")

        // Act / Assert
        assertThrows<IllegalArgumentException> { markdownParser.parse(inputFile) }
    }

    @Test
    @DisplayName("Parse a file with an invalid header")
    fun `Should parse a file with an invalid header`() {
        // Arrange
        val inputFile = getFile(".changes_test/1/1530.md")

        // Act
        assertThrows<IllegalArgumentException> { markdownParser.parse(inputFile) }
    }

    @Test
    @DisplayName("Fail to parse a file with an empty line in headers")
    fun `Should fail to parse a file with an empty line in headers`() {
        // Arrange
        val inputFile = getFile(".changes_test/1/1594.md")

        // Act
        assertThrows<IllegalArgumentException> { markdownParser.parse(inputFile) }
    }

}