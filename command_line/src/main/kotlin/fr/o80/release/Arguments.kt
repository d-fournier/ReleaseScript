package fr.o80.release

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class Arguments(parser: ArgParser) {
    val folder by parser.storing(
        "-f", "--folder",
        help = "Folder with the revision"
    )
        .default(".change")

    val versions by parser.positionalList("VERSION",
        sizeRange = 1..Int.MAX_VALUE,
        help = "..."
    )
        .default(emptyList())
}
