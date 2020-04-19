package ir

class Register: Operand {
    companion object {
        private var count = 0
        val ZERO = Register()
    }
    private val index = count++
    override fun toString(): String {
        return "r$index"
    }
    override fun toWC(): UInt {
        return index.toUInt()
    }
}