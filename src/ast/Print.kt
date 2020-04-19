package ast

class Print(private val src: Expression): Statement {
    override fun toS(n: Int): String {
        return "(print " + src.toS(n) + ')'
    }
    override fun toIR(): ir.Instruction {
        return ir.Print(src.toIR())
    }
}