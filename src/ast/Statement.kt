package ast

interface Statement: Node {
    fun toIR(): ir.Instruction
}