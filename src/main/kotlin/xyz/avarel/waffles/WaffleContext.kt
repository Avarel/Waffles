package xyz.avarel.waffles

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.MaterializedFrame
import xyz.avarel.waffles.nodes.BuiltinNode
import xyz.avarel.waffles.nodes.builtin.PrintlnBuiltinNodeFactory

class WaffleContext {
    val globalFrameDescriptor = FrameDescriptor()

    val globalFrame: MaterializedFrame = Truffle.getRuntime().createVirtualFrame(emptyArray(), globalFrameDescriptor).also {
        val desc = it.frameDescriptor
        it.setInt(desc.addFrameSlot("ONE"), 1)
        it.setObject(desc.addFrameSlot("println"), BuiltinNode.createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(), desc))
    }.materialize()
}