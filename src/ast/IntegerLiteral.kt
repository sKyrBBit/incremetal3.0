package ast

import ir.IRPrinter

class IntegerLiteral(private val value: Int): Expression {
    override fun toS(n: Int): String {
        return "$value"
    }
    override fun toIR(): ir.Operand {
        val tmp = IRPrinter.tmp()
        IRPrinter.add(ir.Three(tmp, 0x20, ir.Register.ZERO, ir.Immediate(value)))
        return tmp
    }
}