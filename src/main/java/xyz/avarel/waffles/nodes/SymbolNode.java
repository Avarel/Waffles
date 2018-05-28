package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class SymbolNode extends WaffleNode {
    public abstract FrameSlot getSlot();

    public interface FrameGet<T> {
        T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    public <T> T readUpStack(FrameGet<T> getter, Frame frame) throws FrameSlotTypeException {
        FrameSlot slot = getSlot();
        T value = getter.get(frame, slot);
        while (value == null) {
            frame = getLexicalScope(frame);
            if (frame == null) {
                throw new RuntimeException("Unknown variable: " + slot.getIdentifier());
            }
            value = getter.get(frame, slot);
        }
        return value;
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readInt(VirtualFrame frame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getInt, frame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame frame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getBoolean, frame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getObject, frame);
    }

    @Specialization(replaces = {"readInt", "readBoolean", "readObject"})
    protected Object read(VirtualFrame virtualFrame) {
        try {
            return this.readUpStack(Frame::getValue, virtualFrame);
        } catch (FrameSlotTypeException ignore) {
            return null;
        }
    }

    private Frame getLexicalScope(Frame frame) {
        return (Frame) frame.getArguments()[0];
    }
}
