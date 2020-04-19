package token

class IntegerLiteral(val value: Int): Token(INTEGER) {
    override fun toString(): String {
        return "$value"
    }
}