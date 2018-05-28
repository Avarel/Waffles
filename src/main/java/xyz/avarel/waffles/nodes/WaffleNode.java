package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import xyz.avarel.waffles.WaffleFunction;
import xyz.avarel.waffles.WaffleTypesGen;

public abstract class WaffleNode extends Node {
    public abstract Object execute(VirtualFrame virtualFrame);

    public int executeInt(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return WaffleTypesGen.expectInteger(execute(virtualFrame));
    }

    public boolean executeBoolean(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return WaffleTypesGen.expectBoolean(execute(virtualFrame));
    }

    public WaffleFunction executeFunction(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return WaffleTypesGen.expectWaffleFunction(execute(virtualFrame));
    }

    protected boolean isArgumentIndexInRange(VirtualFrame virtualFrame, int index) {
        return index < virtualFrame.getArguments().length - 1;
    }

    protected Object getArgument(VirtualFrame virtualFrame, int index) {
        return virtualFrame.getArguments()[index + 1];
    }
}
