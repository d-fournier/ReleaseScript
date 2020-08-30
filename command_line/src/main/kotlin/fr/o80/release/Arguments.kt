package fr.o80.release

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class Arguments(parser: ArgParser) {
    val folder by parser.storing(
        "-f", "--folder",
        help = "Folder with the revision"
    )
        .default(".changes")

    val versions by parser.positionalList(
        "VERSION",
        sizeRange = 1..Int.MAX_VALUE,
        help = "Versions used for changelog generation"
    )
        .default(emptyList())

    val ignoreSlug by parser.flagging(
        "-i", "--ignore-slug",
        help = "Ignore the changes slug (Filename)"
    )
}
