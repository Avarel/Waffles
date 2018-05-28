package xyz.avarel.waffles;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import xyz.avarel.waffles.nodes.BuiltinNode;
import xyz.avarel.waffles.nodes.ReadArgumentNode;
import xyz.avarel.waffles.nodes.WaffleNode;
import xyz.avarel.waffles.nodes.WaffleRootNode;

public class WaffleFunction {
    private final RootCallTarget callTarget;
    private MaterializedFrame lexicalScope;

    public WaffleFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public MaterializedFrame getLexicalScope() {
        return lexicalScope;
    }

    public void setLexicalScope(MaterializedFrame lexicalScope) {
        this.lexicalScope = lexicalScope;
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public static WaffleFunction create(FrameSlot[] arguments, WaffleNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        return new WaffleFunction(
                Truffle.getRuntime().createCallTarget(
                        WaffleRootNode.create(arguments, bodyNodes, frameDescriptor)
                )
        );
    }

    public static WaffleFunction createBuiltinFunction(NodeFactory<? extends BuiltinNode> factory, FrameDescriptor frameDescriptor) {
        int argumentCount = factory.getExecutionSignature().size();
        WaffleNode[] argumentNodes = new WaffleNode[argumentCount];

        for (int i = 0; i < argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }

        BuiltinNode node = factory.createNode((Object) argumentNodes);

        return new WaffleFunction(Truffle.getRuntime().createCallTarget(new WaffleRootNode(frameDescriptor, new WaffleNode[]{node})));
    }
}
