package ir

class Three(private val dst: Operand, code: Byte, private val src1: Operand, private val src2: Operand): Instruction(code) {
    // TODO check if dst & src1 are registers
    override fun toString(): String {
        val op = when (code.toInt()) {
            0x10, 0x20 -> "+"
            0x11, 0x21 -> "-"
            0x12, 0x22 -> "*"
            0x13, 0x23 -> "/"
            0x14 -> ">"
            0x15 -> ">="
            0x16 -> "="
            0x18 -> "&&"
            0x19 -> "||"
            0x1c -> "<<"
            0x1d -> ">>"
            else -> "$code"
        }
        return "$dst = $src1 $op $src2"
    }
    override fun toWC(): UInt {
        return code.toUInt().shl(24) + dst.toWC().shl(16) + src1.toWC().shl(8) + src2.toWC()
    }
}
