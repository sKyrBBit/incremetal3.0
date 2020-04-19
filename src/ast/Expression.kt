package ast

interface Expression: Node {
    fun toIR(): ir.Operand
}