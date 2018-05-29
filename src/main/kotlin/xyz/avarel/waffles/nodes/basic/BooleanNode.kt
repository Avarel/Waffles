package xyz.avarel.waffles.nodes.basic

import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.WaffleNode

class BooleanNode private constructor(private val value: Boolean) : WaffleNode() {
    companion object {
        val TRUE = BooleanNode(true)
        val FALSE = BooleanNode(false)
    }

    override fun execute(virtualFrame: VirtualFrame) = value

    override fun executeBoolean(virtualFrame: VirtualFrame) = value

    override fun toString() = value.toString()
}
