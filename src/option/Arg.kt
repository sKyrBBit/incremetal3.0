package option

class Arg(val long: String) {
    private var description: String? = null
    var short: String? = null
    var takesValue = false
//    private var required = false

    fun description(s: String): Arg {
        description = s
        return this
    }
    fun short(s: String): Arg {
        short = s
        return this
    }
    fun takesValue(b: Boolean): Arg {
        takesValue = b
        return this
    }
    private fun head(): String {
        var builder = ""
        if (short != null) builder += "-$short, "
        return "$builder--$long"
    }
    fun lengthOfHead(): Int {
        return head().length
    }
    fun coordinate(n: Int): String {
        return head() + if (description.isNullOrBlank()) "" else " ".repeat(2 + n) + description
    }
}