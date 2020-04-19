package ast

class ASTPrinter {
    companion object {
        fun tab(n: Int): String {
            // TODO improve tab system
            return " ".repeat(Math.max(0, n))
        }

        fun coordinate(s: String, n: Int): String? {
            return (if (s.length > 4) System.getProperty("line.separator") + tab(n) else ' ').toString() + s + ')'
        }

        fun coordinate(node: Node, n: Int): String? {
            return coordinate(node.toS(n), n)
        }

        fun coordinate(nodes: Collection<Node>, n: Int): String? {
            val builder = StringBuilder()
            val iterator = nodes.iterator()
            while (true) {
                val node = iterator.next()
                builder.append(node.toS(n))
                if (!iterator.hasNext()) {
                    break
                }
                builder.append(System.getProperty("line.separator"))
                        .append(tab(n))
            }
            return builder.toString()
        }
    }
}