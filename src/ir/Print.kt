package ir

class Print(private val src: Operand): Instruction(0xfe.toByte()) {
    override fun toString(): String {
        return "print $src"
    }
    override fun toWC(): UInt {
        return 0xfe000000.toUInt() + src.toWC().shl(16)
    }
}