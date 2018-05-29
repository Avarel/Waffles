package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadArgumentNode extends WaffleNode {
    private final int index;

    public ReadArgumentNode(int index) {
        this.index = index;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (!isArgumentIndexInRange(virtualFrame, index)) {
            throw new IllegalArgumentException("Not enough arguments passed");
        }
        return getArgument(virtualFrame, index);
    }
}
