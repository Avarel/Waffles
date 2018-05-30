package xyz.avarel.waffles

import com.oracle.truffle.api.RootCallTarget
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.MaterializedFrame
import xyz.avarel.waffles.nodes.WaffleNode
import xyz.avarel.waffles.nodes.WaffleRootNode

class WaffleFunction(val callTarget: RootCallTarget) {
    var lexicalScope: MaterializedFrame? = null

    companion object {
        fun create(arguments: Array<FrameSlot>, bodyNodes: Array<WaffleNode>, frameDescriptor: FrameDescriptor): WaffleFunction {
            return WaffleFunction(Truffle.getRuntime().createCallTarget(WaffleRootNode.create(arguments, bodyNodes, frameDescriptor)))
        }
    }
}
