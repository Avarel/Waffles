package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import xyz.avarel.waffles.WaffleFunction;

public class InvokeNode extends WaffleNode {
    @Child
    private WaffleNode functionNode;
    @Children
    private final WaffleNode[] argumentNodes;
    @Child
    private IndirectCallNode callNode;

    public InvokeNode(WaffleNode functionNode, WaffleNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.callNode = Truffle.getRuntime().createIndirectCallNode();
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        WaffleFunction function = evaluateFunction(virtualFrame);
        CompilerAsserts.compilationConstant(argumentNodes.length);

        Object[] argumentValues = new Object[argumentNodes.length + 1];
        argumentValues[0] = function.getLexicalScope();
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i + 1] = argumentNodes[i].execute(virtualFrame);
        }

        return callNode.call(function.getCallTarget(), argumentValues);
    }

    private WaffleFunction evaluateFunction(VirtualFrame virtualFrame) {
        try {
            return functionNode.executeFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new UnsupportedSpecializationException(this, new Node[] { functionNode });
        }
    }
}
