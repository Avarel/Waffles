package xyz.avarel.waffles

class Tuple(private val values: Array<Any>) {
    companion object {
        val EMPTY = Tuple(emptyArray())
    }

    override fun toString() = values.joinToString(prefix = "(", postfix = ")")
}