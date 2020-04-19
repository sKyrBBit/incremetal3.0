package option

class App(name: String): Command(name) {
    private var version = "0.1.0"
    private val authors: MutableList<String> = mutableListOf()
    private val subCommands: MutableList<Command> = mutableListOf()

    init {
        arg(Arg("help")
                .description("show help information")
                .short("h"))
        arg(Arg("version")
                .description("show version information")
                .short("V"))
    }

    fun version(s: String): App {
        version = s
        return this
    }
    fun author(s: String): App {
        authors.add(s)
        return this
    }
    override fun description(s: String): App {
        super.description(s)
        return this
    }
    override fun arg(a: Arg): App {
        super.arg(a)
        return this
    }
    fun subCommand(s: Command): App {
        subCommands.add(s)
        return this
    }
    override fun matches(opts: Array<String>): Match {
        // match sub commands
        val tmp = subCommands.firstOrNull() { sub -> sub.name == opts[0] }
        if (tmp != null) return Match(tmp.name, tmp.matches(opts.drop(1).toTypedArray()))

        // match args
        val match = super.matches(opts)
        when {
            match.flags.any { arg -> arg == "help" } -> {
                println("""
                   $name $version
                   ${authors.joinToString(", ")}
               """.trimIndent())
                if (description != null) println(description)
                if (args.isNotEmpty()) {
                    println("FRAGS:")
                    val max = args.map(Arg::lengthOfHead).max()!!
                    println(args.joinToString(System.getProperty("line.separator"))
                    { arg -> "\t" + arg.coordinate(max - arg.lengthOfHead()) })
                }
                if (subCommands.isNotEmpty()) {
                    println("SUBCOMMANDS:")
                    val max = subCommands.map(Command::lengthOfName).max()!!
                    println(subCommands.joinToString(System.getProperty("line.separator"))
                    { sub -> "\t" + sub.coordinate(max - sub.lengthOfName()) })
                }
            }
            match.flags.any { arg -> arg == "version" } -> {
                println("$name $version")
            }
        }
        return match
    }
}