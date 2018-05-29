package xyz.avarel.waffles.nodes.basic;

import com.oracle.truffle.api.frame.VirtualFrame;
import xyz.avarel.waffles.nodes.WaffleNode;

public class BooleanNode extends WaffleNode {
    public static final BooleanNode TRUE = new BooleanNode(true);
    public static final BooleanNode FALSE = new BooleanNode(false);

    private final boolean value;

    private BooleanNode(boolean value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
