package token

open class Token(val tag: Int) {
    private var tagToString: () -> String = { "<$tag>" }
    companion object {
        @JvmField val EOF: Token = Token(-1, "EOF")
        @JvmField val EOL: Token = Token(-2, "EOL")
        const val INTEGER = 256
        const val IDENTIFIER = 257
        const val AND = 258
        const val OR = 259
        const val GE = 260
        const val LE = 261
        const val ASSIGN = 262
        const val PRINT = 263
        const val BOOLEAN = 264
    }
    constructor(tag: Int, id: String): this(tag) {
        tagToString = { "<$id>" }
    }
    constructor(tag: Char): this(tag.toInt())
    override fun toString(): String {
        return if (tag in 0x20..0x7f) tag.toChar().toString() else tagToString()
    }
}