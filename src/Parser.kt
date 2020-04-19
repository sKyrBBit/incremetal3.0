import token.*
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.FileOutputStream
import kotlin.reflect.jvm.internal.impl.resolve.constants.UIntValue

class Parser (private val lexer: Lexer) {
    private var row = 1
    private var column = 1
    var line = ""
    private lateinit var token: Token
    companion object {
        @JvmStatic
        fun parse(path: String) {
            val parser = Parser(Lexer("$path.grp"))
            // initialize
            parser.row = 1
            parser.column = 1
            parser.token = parser.lexer.nextToken()
            try {
                WCPrinter.print(path, parser.parseClosure().toIR().map(ir.Instruction::toWC))
            } catch (e: ParsingException) {
                e.print()
            }
        }
        @JvmStatic
        fun parseD(path: String) {
            val parser = Parser(Lexer("$path.grp"))
            // initialize
            parser.row = 1
            parser.column = 1
            parser.token = parser.lexer.nextToken()
            try {
                val astResult = parser.parseClosure()
                println(astResult.toS(0))
                val irResult = astResult.toIR()
                println(irResult)
                val wcResult = irResult.map(ir.Instruction::toWC)
                WCPrinter.print(path, wcResult)
            } catch (e: ParsingException) {
                e.print()
            }
        }
    }
    private fun parseClosure(): ast.Closure {
        consume('{')
        consume(-2)
        val stmts = ArrayList<ast.Statement>()
        while(!match('}')) {
            val stmt = parseStatement()
            stmts.add(stmt)
        }
        consume('}')
        return ast.Closure(stmts)
    }
    private fun parseStatement(): ast.Statement {
        return when {
            match(Token.PRINT) -> {
                consume()
                val stmt = ast.Print(parseExpression())
                if (match(-1) || match(-2)) {
                    consume()
                    stmt
                } else {
                    throw ParsingException(row, column, line, "expected <EOL> or <EOF>, found $token")
                }
            }
            else -> {
                val dst = ast.VarRef(consumeIdentifier())
                consume(Token.ASSIGN)
                val src1 = parseFactor()
                val stmt = when {
                    match('+') -> { consume(); ast.Assign(dst, "+", src1, parseExpression()) }
                    match('-') -> { consume(); ast.Assign(dst, "-", src1, parseExpression()) }
                    match('*') -> { consume(); ast.Assign(dst, "*", src1, parseExpression()) }
                    match('/') -> { consume(); ast.Assign(dst, "/", src1, parseExpression()) }
                    match('>') -> { consume(); ast.Assign(dst, ">", src1, parseExpression()) }
                    match(Token.GE) -> { consume(); ast.Assign(dst, ">=", src1, parseExpression()) }
                    match('<') -> { consume(); ast.Assign(dst, "<", src1, parseExpression()) }
                    match(Token.LE) -> { consume(); ast.Assign(dst, "<=", src1, parseExpression()) }
                    match('=') -> { consume(); ast.Assign(dst, "=", src1, parseExpression()) }
                    match(Token.AND) -> { consume(); ast.Assign(dst, "&&", src1, parseExpression()) }
                    match(Token.OR) -> { consume(); ast.Assign(dst, "||", src1, parseExpression()) }
                    else -> throw ParsingException(row, column, line, "expected <EOL> or <EOF>, found $token")
                }
                if (match(-1) || match(-2)) {
                    consume()
                    stmt
                } else {
                    throw ParsingException(row, column, line, "expected <EOL> or <EOF>, found $token")
                }
            }
        }
    }
    private fun parseExpression(): ast.Expression {
        var left = parseTerm()
        while (true) {
            left = when {
                match('>') -> { consume(); ast.BinaryOperator(">", left, parseExpression()) }
                match(Token.GE) -> { consume(); ast.BinaryOperator(">=", left, parseExpression()) }
                match('<') -> { consume(); ast.BinaryOperator("<", left, parseExpression()) }
                match(Token.LE) -> { consume(); ast.BinaryOperator("<=", left, parseExpression()) }
                match('=') -> { consume(); ast.BinaryOperator("=", left, parseExpression()) }
                match(Token.AND) -> { consume(); ast.BinaryOperator("&&", left, parseExpression()) }
                match(Token.OR) -> { consume(); ast.BinaryOperator("||", left, parseExpression()) }
                else -> return left
            }
        }
    }
    private fun parseTerm(): ast.Expression {
        var left = parseFactor()
        while (true) {
            left = when {
                match('+') -> { consume(); ast.BinaryOperator("+", left, parseTerm()) }
                match('-') -> { consume(); ast.BinaryOperator("-", left, parseTerm()) }
                match('*') -> { consume(); ast.BinaryOperator("*", left, parseTerm()) }
                match('/') -> { consume(); ast.BinaryOperator("/", left, parseTerm()) }
                else -> return left
            }
        }
    }
    private fun parseFactor(): ast.Expression {
        return when {
            match('(') -> {
                val expr = parseExpression()
                consume(')')
                expr
            }
            match(Token.INTEGER) -> ast.IntegerLiteral(consumeInteger())
            match(Token.BOOLEAN) -> ast.BooleanLiteral(consumeBoolean())
            match(Token.IDENTIFIER) -> ast.VarRef(consumeIdentifier())
            else -> throw ParsingException(row, column, line, "expected factor, found $token")
        }
    }
    private fun match(tag: Int): Boolean {
        return token.tag == tag
    }
    private fun match(tag: Char): Boolean {
        return match(tag.toInt())
    }
    private fun consume() { // TODO
        row = lexer.row
        column = lexer.column
        line = lexer.line
        token = lexer.nextToken()
    }
    /** @throws ClassCastException {@code match(token.Token.INTEGER)} */
    private fun consumeInteger(): Int {
        val value = (token as IntegerLiteral).value
        consume()
        return value
    }
    /** @throws ClassCastException {@code match(token.Token.BOOLEAN)} */
    private fun consumeBoolean(): Boolean {
        val value = (token as BooleanLiteral).value
        consume()
        return value
    }
    /** @throws ClassCastException {@code match(token.Token.IDENTIFIER)} */
    private fun consumeIdentifier(): String {
        val name = (token as Identifier).name
        consume()
        return name
    }
    private fun consume(tag: Int) {
        when {
            match(tag) -> consume()
            tag in 0x20..0x7f -> throw ParsingException(row, column, line, "expected " + tag.toChar() + ", found $token")
            else -> throw ParsingException(row, column, line, "expected <$tag>, found $token")
        }
    }
    private fun consume(tag: Char) {
        consume(tag.toInt())
    }
}