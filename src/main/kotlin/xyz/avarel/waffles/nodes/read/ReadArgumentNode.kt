package xyz.avarel.waffles.nodes.read

import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.WaffleNode

class ReadArgumentNode(private val index: Int) : WaffleNode() {
    override fun execute(virtualFrame: VirtualFrame): Any {
        if (!isArgumentIndexInRange(virtualFrame, index)) {
            throw IllegalArgumentException("Not enough arguments passed")
        }
        return getArgument(virtualFrame, index)
    }
}
