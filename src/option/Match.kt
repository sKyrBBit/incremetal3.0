package option

class Match {
    val flags: Set<String>
    private val options: Set<Option>
    private val subName: String?
    private val subMatch: Match?
    fun contains(s: String): Boolean {
         return flags.any { f -> f == s }
    }
    fun getValue(s: String): String? {
        return options.firstOrNull { o -> o.name == s }?.value
    }
    fun getSub(s: String): Match? {
        return if (subName == s) subMatch else null
    }
    constructor(f: Set<String>, o: Set<Option>) {
        flags = f
        options = o
        subName = null
        subMatch = null
    }
    constructor(name: String, match: Match) {
        flags = mutableSetOf()
        options = mutableSetOf()
        subName = name
        subMatch = match
    }
}