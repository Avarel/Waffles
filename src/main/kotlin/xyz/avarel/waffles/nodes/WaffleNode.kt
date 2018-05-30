package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.Node
import xyz.avarel.waffles.WaffleFunction
import xyz.avarel.waffles.WaffleTypesGen

abstract class WaffleNode : Node() {
//    var sourceSection: SourceSection? = null

    abstract fun execute(virtualFrame: VirtualFrame): Any

//    open fun executeVoid(virtualFrame: VirtualFrame) {
//
//    }

    open fun executeInt(virtualFrame: VirtualFrame): Int {
        return WaffleTypesGen.expectInteger(execute(virtualFrame))
    }

    open fun executeString(virtualFrame: VirtualFrame): String {
        return WaffleTypesGen.expectString(execute(virtualFrame))
    }

    open fun executeBoolean(virtualFrame: VirtualFrame): Boolean {
        return WaffleTypesGen.expectBoolean(execute(virtualFrame))
    }

    fun executeFunction(virtualFrame: VirtualFrame): WaffleFunction {
        return WaffleTypesGen.expectWaffleFunction(execute(virtualFrame))
    }

    protected fun isArgumentIndexInRange(virtualFrame: VirtualFrame, index: Int): Boolean {
        return index < virtualFrame.arguments.size - 1
    }

    protected fun getArgument(virtualFrame: VirtualFrame, index: Int): Any {
        return virtualFrame.arguments[index + 1]
    }
}
