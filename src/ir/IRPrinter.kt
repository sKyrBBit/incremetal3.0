package ir

class IRPrinter {
    companion object {
        val instrs = ArrayList<Instruction>()
        fun tmp(): Register {
            return Register()
        }
        fun add(instr: Instruction) {
            instrs.add(instr)
        }
    }
}