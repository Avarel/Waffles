package xyz.avarel.waffles;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import xyz.avarel.waffles.nodes.InvokeNode;
import xyz.avarel.waffles.nodes.SymbolNodeGen;
import xyz.avarel.waffles.nodes.basic.IntNode;
import xyz.avarel.waffles.nodes.WaffleNode;
import xyz.avarel.waffles.nodes.builtin.AddBuiltinNodeFactory;
import xyz.avarel.waffles.nodes.builtin.PrintlnBuiltinNodeFactory;

public class Main {
    public static void main(String[] args) {
        VirtualFrame topFrame = createTopFrame(new FrameDescriptor());
        WaffleNode node = new InvokeNode(SymbolNodeGen.create(topFrame.getFrameDescriptor().findFrameSlot("println")), new WaffleNode[]{ new IntNode(3) });//AddBuiltinNodeFactory.create(new WaffleNode[] { new IntNode(100), new IntNode(6) });
        System.out.println(
                execute(
                        node,
                       topFrame));
    }

    private static Object execute(WaffleNode node, VirtualFrame topFrame) {
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        WaffleFunction function = WaffleFunction.create(new FrameSlot[] {}, new WaffleNode[] { node }, frameDescriptor);
        DirectCallNode dcn = Truffle.getRuntime().createDirectCallNode(function.getCallTarget());
        return dcn.call(new Object[] { topFrame.materialize() });
    }

    private static VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(new Object[] {}, frameDescriptor);
        virtualFrame.setObject(frameDescriptor.addFrameSlot("println"), WaffleFunction.createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(), frameDescriptor));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("+"), WaffleFunction.createBuiltinFunction(AddBuiltinNodeFactory.getInstance(), frameDescriptor));
        return virtualFrame;
    }
}
