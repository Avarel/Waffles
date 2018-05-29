package xyz.avarel.waffles

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.InvokeNode
import xyz.avarel.waffles.nodes.SymbolNodeGen
import xyz.avarel.waffles.nodes.WaffleNode
import xyz.avarel.waffles.nodes.basic.IntNode
import xyz.avarel.waffles.nodes.builtin.AddBuiltinNodeFactory
import xyz.avarel.waffles.nodes.builtin.PrintlnBuiltinNodeFactory

fun main(args: Array<String>) {
    val topFrame = createTopFrame(FrameDescriptor())
    val node = InvokeNode(SymbolNodeGen.create(topFrame.frameDescriptor.findFrameSlot("wafflePrintln")), arrayOf(IntNode(3)))//AddBuiltinNodeFactory.create(new WaffleNode[] { new IntNode(100), new IntNode(6) });
    println(execute(node, topFrame))
}

private fun execute(node: WaffleNode, topFrame: VirtualFrame): Any {
    val frameDescriptor = topFrame.frameDescriptor
    val function = WaffleFunction.create(arrayOf(), arrayOf(node), frameDescriptor)
    val dcn = Truffle.getRuntime().createDirectCallNode(function.callTarget)
    return dcn.call(arrayOf(topFrame))
}

private fun createTopFrame(frameDescriptor: FrameDescriptor): VirtualFrame {
    val virtualFrame = Truffle.getRuntime().createVirtualFrame(arrayOf(), frameDescriptor)
    virtualFrame.setObject(frameDescriptor.addFrameSlot("wafflePrintln"), WaffleFunction.createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(), frameDescriptor))
    virtualFrame.setObject(frameDescriptor.addFrameSlot("+"), WaffleFunction.createBuiltinFunction(AddBuiltinNodeFactory.getInstance(), frameDescriptor))
    return virtualFrame
}
