package ast

class Assign(private val dst: VarRef, private val op: String, private val src1: Expression, private val src2: Expression): Statement {
    override fun toS(n: Int): String {
        val x = n + 10 + op.length
        val tmp = '(' + op + ' ' + src1.toS(x) + ASTPrinter.coordinate(src2, x)
        return "(assign " + dst.toS(n + 8) + ASTPrinter.coordinate(tmp, n + 8)
    }
    override fun toIR(): ir.Instruction {
        return when (op) {
            "<" -> ir.Three(dst.toIR(), 0x14, src2.toIR(), src1.toIR())
            "<=" -> ir.Three(dst.toIR(), 0x15, src2.toIR(), src1.toIR())
            else -> ir.Three(dst.toIR(), when (op) {
                "+" -> 0x10
                "-" -> 0x11
                "*" -> 0x12
                "/" -> 0x13
                ">" -> 0x14
                ">=" -> 0x15
                "=" -> 0x16
                "&&" -> 0x18
                "||" -> 0x19
                else -> 0x00
            }, src1.toIR(), src2.toIR())
        }
    }
}