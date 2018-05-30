package xyz.avarel.waffles

import org.graalvm.polyglot.Context

//fun main(args: Array<String>) {
//
//}

fun main(args: Array<String>) {
    Context.create("waffles").use {
        it.eval("waffles", "testWHY")
    }
    return
//    val topFrame = createTopFrame(FrameDescriptor())
////    AddBuiltinNodeFactory.create(arrayOf(IntNode(3), IntNode(400)));
//    val node = InvokeNode(SymbolNodeGen.create(topFrame.frameDescriptor.findFrameSlot("println")), arrayOf(IntNode(3)))//AddBuiltinNodeFactory.create(new WaffleNode[] { new IntNode(100), new IntNode(6) });
//    println(execute(node, topFrame))
}
//
//private fun execute(node: WaffleNode, topFrame: VirtualFrame): Any {
//    val frameDescriptor = topFrame.frameDescriptor
//    val function = WaffleFunction.create(arrayOf(), arrayOf(node), frameDescriptor)
//    val dcn = Truffle.getRuntime().createDirectCallNode(function.callTarget)
//    return dcn.call(arrayOf(topFrame))
//}
//
//private fun createTopFrame(frameDescriptor: FrameDescriptor): VirtualFrame {
//    val virtualFrame = Truffle.getRuntime().createVirtualFrame(arrayOf(), frameDescriptor)
//    virtualFrame.setObject(frameDescriptor.addFrameSlot("println"), BuiltinNode.createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(), frameDescriptor))
//    virtualFrame.setObject(frameDescriptor.addFrameSlot("+"), BuiltinNode.createBuiltinFunction(AddBuiltinNodeFactory.getInstance(), frameDescriptor))
//    return virtualFrame
//}
