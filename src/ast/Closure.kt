package ast

import ir.IRPrinter

class Closure(private val stmts: List<Statement>) {
    fun toS(n: Int): String {
        return if (stmts.isEmpty()) {
            "(closure)"
        } else {
            "(closure \n" + ASTPrinter.tab(n + 9) + ASTPrinter.coordinate(stmts, n + 9) + ')'
        }
    }
    fun toIR(): List<ir.Instruction> {
        stmts.map(Statement::toIR).forEach { x -> IRPrinter.add(x) }
        return IRPrinter.instrs
    }
}