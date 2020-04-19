package token

import token.Token

class Identifier(val name: String): Token(IDENTIFIER) {
    override fun toString(): String {
        return "token.Identifier($name)"
    }
}