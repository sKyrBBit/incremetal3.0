package token

class BooleanLiteral(val value: Boolean): Token(BOOLEAN) {
    override fun toString(): String {
        return "$value"
    }
}