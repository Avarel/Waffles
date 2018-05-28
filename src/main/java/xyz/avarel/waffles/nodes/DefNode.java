package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class DefNode extends WaffleNode {
    protected abstract FrameSlot getSlot();

    @Specialization(guards = "isIntKind()")
    protected int writeInt(VirtualFrame virtualFrame, int value) {
        virtualFrame.setInt(getSlot(), value);
        return value;
    }

    @Specialization(guards = "isBooleanKind()")
    protected boolean writeBoolean(VirtualFrame virtualFrame, boolean value) {
        virtualFrame.setBoolean(getSlot(), value);
        return value;
    }

    @Specialization(replaces = {"writeInt", "writeBoolean"})
    protected Object writeObject(VirtualFrame virtualFrame, Object value) {
        FrameSlot slot = getSlot();
        if (slot.getKind() != FrameSlotKind.Object) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            slot.setKind(FrameSlotKind.Object);
        }
        virtualFrame.setObject(slot, value);
        return value;
    }

    protected boolean isIntKind() {
        return getSlot().getKind() == FrameSlotKind.Int;
    }

    protected boolean isBooleanKind() {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }
}

