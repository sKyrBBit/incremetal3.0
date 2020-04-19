import java.lang.Exception

class ParsingException(private val row: Int, private val column: Int, private val line: String,
                       override val message: String): Exception() {
    fun print() {
        val tmp = "$row: "
        println(tmp + line)
        println(" ".repeat(tmp.length + column - 1) + "^ $message")
    }
}