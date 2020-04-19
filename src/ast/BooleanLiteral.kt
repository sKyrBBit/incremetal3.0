package ast

import ir.IRPrinter

class BooleanLiteral(private val value: Boolean): Expression {
    override fun toS(n: Int): String {
        return "$value"
    }
    override fun toIR(): ir.Operand {
        val tmp = IRPrinter.tmp()
        IRPrinter.add(ir.Three(tmp, 0x20, ir.Register.ZERO, ir.Immediate(if (value) 1 else 0)))
        return tmp
    }
}