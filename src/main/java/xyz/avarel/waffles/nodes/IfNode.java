package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfNode extends WaffleNode {
    @Child
    private WaffleNode testNode;
    @Child
    private WaffleNode thenNode;
    @Child
    private WaffleNode elseNode;

    private final ConditionProfile conditionProfile = ConditionProfile.createBinaryProfile();

    public IfNode(WaffleNode testNode, WaffleNode thenNode, WaffleNode elseNode) {
        this.testNode = testNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (this.conditionProfile.profile(testResult(virtualFrame))) {
            return thenNode.execute(virtualFrame);
        } else {
            return elseNode.execute(virtualFrame);
        }
    }

    private boolean testResult(VirtualFrame virtualFrame) {
        try {
            return testNode.executeBoolean(virtualFrame);
        } catch (UnexpectedResultException ignore) {
            return false;
        }
    }
}
