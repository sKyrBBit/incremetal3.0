package ast

import ir.IRPrinter

class BinaryOperator(private val op: String, private val src1: Expression, private val src2: Expression): Expression {
    override fun toS(n: Int): String {
        val x =  n + 2 + op.length
        return '(' + op + ' ' + src1.toS(x) + ASTPrinter.coordinate(src2, x)
    }
    override fun toIR(): ir.Operand {
        val tmp = IRPrinter.tmp()
        IRPrinter.add(
           when (op) {
               "<" -> ir.Three(tmp, 0x14, src2.toIR(), src1.toIR())
               "<=" -> ir.Three(tmp, 0x15, src2.toIR(), src1.toIR())
               else -> ir.Three(tmp, when (op) {
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
           })
        return tmp
    }
}