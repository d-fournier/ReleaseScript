package fr.o80.release.parser.md

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

internal class MarkdownReaderTest {

    @Test
    fun `Should read all headers`() {
        // Arrange
        val inputStream = File(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        reader
            .readHeaders {
                assertThat(it).containsExactly(
                    "type", "feature",
                    "link", "http://example.org/user-story-15423"
                )
            }
    }

    @Test
    fun `Should read 2 lines`() {
        // Arrange
        val inputStream = File(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        reader
            .readLine { line -> assertThat(line).isEqualTo("---") }
            .readLine { line -> assertThat(line).isEqualTo("type: feature") }
    }

    @Test
    fun `Should read headers then next line`() {
        // Arrange
        val inputStream = File(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        reader
            .readHeaders { /* noop */ }
            .readLine { line -> assertThat(line).isEqualTo("Implement the system to do something that is really important") }
    }

    @Test
    fun `Should read headers then the remaining lines`() {
        // Arrange
        val inputStream = File(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)
        val expected = """
                        Implement the system to do something that is really important
                        
                        This system handles:
                        - thing one
                        - the other thing
                        - the rest
                       """.trimIndent()

        // Act / Assert
        reader
            .readHeaders { /* noop */ }
            .readRemaining { line -> assertThat(line).isEqualTo(expected) }
    }

    @Test
    fun `Should fail to read invalid headers block`() {
        // Arrange
        val inputStream = File(".changes_test/1/1645.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        assertThrows<IllegalArgumentException> {
            reader.readHeaders { }
        }
    }
}