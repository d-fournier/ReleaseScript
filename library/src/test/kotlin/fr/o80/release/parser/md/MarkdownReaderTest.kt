package fr.o80.release.parser.md

import com.google.common.truth.Truth.assertThat
import fr.o80.release.Helpers
import fr.o80.release.Helpers.getFile
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

// TODO Revoir TOUS ces tests là pour s'assurer qu'on a bien testé tous les recoins de l'algo
internal class MarkdownReaderTest {

    @Test
    fun `Should read all headers`() {
        // Arrange
        val inputStream = getFile(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act
        val headers = reader.readHeaders()

        // Assert
        assertThat(headers).containsExactly(
            "type", "feature",
            "link", "http://example.org/user-story-15423"
        )
    }

    @Test
    fun `Should read 2 lines`() {
        // Arrange
        val inputStream = getFile(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        val output1 = reader.readLine()
        val output2 = reader.readLine()

        // Assert
        assertThat(output1).isEqualTo("---")
        assertThat(output2).isEqualTo("type: feature")
    }

    @Test
    fun `Should read headers then next line`() {
        // Arrange
        val inputStream = getFile(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)
        reader.readHeaders()

        // Act / Assert
        val output = reader.readLine()

        // Assert
        assertThat(output).isEqualTo("Implement the system to do something that is really important")
    }

    @Test
    fun `Should read headers then the remaining lines`() {
        // Arrange
        val inputStream = getFile(".changes_test/1/15423.md").inputStream()
        val reader = MarkdownReader(inputStream)
        reader.readHeaders()
        val expected = """
                        Implement the system to do something that is really important
                        
                        This system handles:
                        - thing one
                        - the other thing
                        - the rest
                       """.trimIndent()

        // Act
        val output = reader.readRemaining()

        // Assert
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `Should fail to read invalid headers block`() {
        // Arrange
        val inputStream = getFile(".changes_test/1/1645.md").inputStream()
        val reader = MarkdownReader(inputStream)

        // Act / Assert
        assertThrows<IllegalArgumentException> {
            reader.readHeaders()
        }
    }
}