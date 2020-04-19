package ast

class VarRef(private val name: String): Expression {
    companion object {
        private val regs = HashMap<String, ir.Register>()
    }
    override fun toS(n: Int): String {
        return name
    }
    override fun toIR(): ir.Operand {
        return if (regs.containsKey(name)) { regs[name]!! }
        else {
            val tmp = ir.Register()
            regs[name] = tmp
            tmp
        }
    }
}