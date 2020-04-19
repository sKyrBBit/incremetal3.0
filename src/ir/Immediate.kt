package ir

class Immediate(private val value: Int): Operand {
    override fun toString(): String {
        return "$value"
    }
    override fun toWC(): UInt {
        return value.toUInt()
    }
}