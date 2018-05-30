package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.dsl.NodeChild
import com.oracle.truffle.api.dsl.NodeFactory
import com.oracle.truffle.api.frame.FrameDescriptor
import xyz.avarel.waffles.WaffleFunction
import xyz.avarel.waffles.nodes.read.ReadArgumentNode

@NodeChild(value = "arguments", type = Array<WaffleNode>::class)
abstract class BuiltinNode : WaffleNode() {
    companion object {
        fun createBuiltinFunction(factory: NodeFactory<out BuiltinNode>, frameDescriptor: FrameDescriptor): WaffleFunction {
            val argumentCount = factory.executionSignature.size
            val argumentNodes = arrayOfNulls<WaffleNode>(argumentCount)

            for (i in 0 until argumentCount) {
                argumentNodes[i] = ReadArgumentNode(i)
            }

            val node = factory.createNode(argumentNodes)

            return WaffleFunction(Truffle.getRuntime().createCallTarget(WaffleRootNode(frameDescriptor, arrayOf(node))))
        }
    }
}
