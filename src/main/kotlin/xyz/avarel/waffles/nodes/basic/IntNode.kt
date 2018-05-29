package xyz.avarel.waffles.nodes.basic

import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.WaffleNode

class IntNode(private val value: Int) : WaffleNode() {
    override fun execute(virtualFrame: VirtualFrame) = value

    override fun executeInt(virtualFrame: VirtualFrame) = value

    override fun toString() = value.toString()
}
