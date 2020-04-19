import token.*
import java.io.FileInputStream
import java.util.*

class Lexer(path: String) {
    var row = 1
    var column = 1
    var line = ""
    private var lineQ = ArrayDeque<Char>()
    private val scanner: Scanner = Scanner(FileInputStream(path))

    init {
        if (scanner.hasNext()) nextLine()
    }

    @Throws(ParsingException::class)
    fun nextToken(): Token {
        try {
            // skip whitespaces
            while (true) {
                if (lineQ.isEmpty()) {
                    nextLine()
                    return Token.EOL
                }
                if (matchWhitespace()) consume()
                else break
            }
            // tokenize
            return when {
                matchDigit() -> {
                    var builder = ""
                    do {
                        builder += consume()
                    } while (lineQ.isNotEmpty() && matchDigit())
                    IntegerLiteral(builder.toInt())
                }
                matchLetter() -> {
                    var builder = ""
                    do {
                        builder += consume()
                    } while (lineQ.isNotEmpty() && matchLetterOrDigit())
                    when (builder) {
                        "print" -> Print()
                        "true" -> BooleanLiteral(true)
                        "false" -> BooleanLiteral(false)
                        else -> Identifier(builder)
                    }
                }
                match('+') -> { consume(); Token('+') }
                match('-') -> { consume(); Token('-') }
                match('*') -> { consume(); Token('*') }
                match('/') -> { consume(); Token('/') }
                match('>') -> { consume();
                    if (match('=')) { consume(); Token(Token.GE) }
                    else Token('>') }
                match('<') -> { consume();
                    if (match('=')) { consume(); Token(Token.LE) }
                    else Token('<') }
                match('=') -> { consume(); Token('=') }
                match('&') -> { consume()
                    if (match('&')) { consume(); Token(Token.AND) }
                    else Token('&') }
                match('|') -> { consume()
                    if (match('|')) { consume(); Token(Token.OR) }
                    else Token('|') }
                match(':') -> { consume();
                    if (match('=')) { consume(); Token(Token.ASSIGN) }
                    else Token(':') }
                match('(') -> { consume(); Token('(') }
                match(')') -> { consume(); Token(')') }
                match('{') -> { consume(); Token('{') }
                match('}') -> { consume(); Token('}') }
                else -> throw ParsingException(row, column, line, "found unknown token")
            }
        } catch (e: NoSuchElementException) {
            return Token.EOF
        }
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun match(tag: Char): Boolean {
        return lineQ.element() == tag
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun matchDigit(): Boolean {
        return Character.isDigit(lineQ.element())
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun matchLetter(): Boolean {
        return Character.isLetter(lineQ.element())
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun matchLetterOrDigit(): Boolean {
        return Character.isLetterOrDigit(lineQ.element())
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun matchWhitespace(): Boolean {
        return Character.isWhitespace(lineQ.element())
    }
    /** @throws NoSuchElementException {@code lineQ} is empty */
    private fun consume(): Char {
        column++
        return lineQ.remove()
    }
    /** @throws NoSuchElementException {@code scanner} doesn't have the next line */
    private fun nextLine() {
        line = scanner.nextLine()
        line.toCollection(lineQ)
        column = 1
        row++
    }
}