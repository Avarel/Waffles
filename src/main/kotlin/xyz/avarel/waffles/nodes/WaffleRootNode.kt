package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.ExplodeLoop
import com.oracle.truffle.api.nodes.RootNode
import xyz.avarel.waffles.nodes.read.ReadArgumentNode

class WaffleRootNode(frameDescriptor: FrameDescriptor, @field:Children
private val bodyNodes: Array<WaffleNode>) : RootNode(null, frameDescriptor) {

    @ExplodeLoop
    override fun execute(virtualFrame: VirtualFrame): Any {
        val secondToLast = bodyNodes.size - 1
        CompilerAsserts.compilationConstant<Any>(secondToLast)
        for (i in 0 until secondToLast) {
            bodyNodes[i].execute(virtualFrame)
        }
        return bodyNodes[secondToLast].execute(virtualFrame)
    }

    companion object {
        fun create(argumentNames: Array<FrameSlot>, bodyNodes: Array<WaffleNode>, frameDescriptor: FrameDescriptor): WaffleRootNode {
            val allNodes = arrayOfNulls<WaffleNode>(argumentNames.size + bodyNodes.size)

            for (i in argumentNames.indices) {
                allNodes[i] = DefNodeGen.create(ReadArgumentNode(i), argumentNames[i])
            }

            System.arraycopy(bodyNodes, 0, allNodes, argumentNames.size, bodyNodes.size)

            return WaffleRootNode(frameDescriptor, allNodes.requireNoNulls())
        }
    }
}
