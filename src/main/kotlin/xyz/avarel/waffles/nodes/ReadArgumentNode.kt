package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.frame.VirtualFrame

class ReadArgumentNode(private val index: Int) : WaffleNode() {
    override fun execute(virtualFrame: VirtualFrame): Any {
        if (!isArgumentIndexInRange(virtualFrame, index)) {
            throw IllegalArgumentException("Not enough arguments passed")
        }
        return getArgument(virtualFrame, index)
    }
}
