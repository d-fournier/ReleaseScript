package fr.o80.release.parser

import java.io.File

interface Parser {
    fun parse(file: File): ParsedFile
}