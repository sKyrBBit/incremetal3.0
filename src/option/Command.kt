package option

open class Command(val name: String) {
    var description: String? = null
    val args: MutableList<Arg> = mutableListOf()
    open fun description(s: String): Command {
        description = s
        return this
    }
    open fun arg(a: Arg): Command {
        args.add(a)
        return this
    }
    fun lengthOfName(): Int {
        return name.length
    }
    fun coordinate(n: Int): String {
        return name + if (description.isNullOrBlank()) "" else " ".repeat(2 + n) + description
    }
    open fun matches(opts: Array<String>): Match {
        val flags = mutableSetOf<String>()
        val options = mutableSetOf<Option>()
        var i = 0
        while (i < opts.size) {
            val arg = args.first { arg -> opts[i] == "--${arg.long}" || opts[i] == "-${arg.short}" }
            if (arg.takesValue) {
                options.add(Option(arg.long, opts[i + 1]))
                i += 2
            } else {
                flags.add(arg.long)
                i++
            }
        }
        return Match(flags, options)
    }
}