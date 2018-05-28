package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class WaffleRootNode extends RootNode {
    @Children
    private final WaffleNode[] bodyNodes;

    public WaffleRootNode(FrameDescriptor frameDescriptor, WaffleNode[] bodyNodes) {
        super(null, frameDescriptor);
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        int secondToLast = bodyNodes.length - 1;
        CompilerAsserts.compilationConstant(secondToLast);
        for (int i = 0; i < secondToLast; i++) {
            bodyNodes[i].execute(virtualFrame);
        }
        return bodyNodes[secondToLast].execute(virtualFrame);
    }

    public static WaffleRootNode create(FrameSlot[] argumentNames, WaffleNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        WaffleNode[] allNodes = new WaffleNode[argumentNames.length + bodyNodes.length];

        for (int i = 0; i < argumentNames.length; i++) {
            allNodes[i] = DefNodeGen.create(new ReadArgumentNode(i), argumentNames[i]);
        }

        System.arraycopy(bodyNodes, 0, allNodes, argumentNames.length, bodyNodes.length);

        return new WaffleRootNode(frameDescriptor, allNodes);
    }
}
