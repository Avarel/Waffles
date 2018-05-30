package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.RootCallTarget
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.DirectCallNode
import com.oracle.truffle.api.nodes.RootNode
import xyz.avarel.waffles.WaffleLanguage

class WaffleEvalRootNode(language: WaffleLanguage, rootFunction: RootCallTarget) : RootNode(null) {
    private val reference = language.contextReference
    private val mainCallNode = DirectCallNode.create(rootFunction)

    override fun execute(frame: VirtualFrame): Any {
        val topFrame = reference.get().globalFrame
        println("TEST$topFrame")
        topFrame.getInt(topFrame.frameDescriptor.findFrameSlot("ONE"))
        return mainCallNode.call(arrayOf(topFrame))
    }
}
