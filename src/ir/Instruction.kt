package ir

abstract class Instruction(val code: Byte) {
    abstract fun toWC(): UInt
}