package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class IntNode extends WaffleNode {
    private final int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public int executeInt(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
