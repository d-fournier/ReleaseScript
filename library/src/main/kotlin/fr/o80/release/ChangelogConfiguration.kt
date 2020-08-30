package fr.o80.release

data class ChangelogConfiguration(
    val ignoreSlug: Boolean,
    val validTypes: List<String> = VALID_TYPES
)

private val VALID_TYPES = listOf("feature", "fix")
