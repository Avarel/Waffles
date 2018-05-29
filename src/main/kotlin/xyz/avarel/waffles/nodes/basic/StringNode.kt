package xyz.avarel.waffles.nodes.basic

import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.WaffleNode

class StringNode(private val value: String) : WaffleNode() {
    override fun execute(virtualFrame: VirtualFrame) = value

    override fun executeString(virtualFrame: VirtualFrame) = value

    override fun toString() = value
}
