package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.dsl.NodeChild
import com.oracle.truffle.api.dsl.NodeField
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.FrameSlotKind
import com.oracle.truffle.api.frame.VirtualFrame

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot::class)
abstract class DefNode : WaffleNode() {
    protected abstract val slot: FrameSlot

    protected val isIntKind: Boolean get() = slot.kind == FrameSlotKind.Int
    protected val isBooleanKind: Boolean get() = slot.kind == FrameSlotKind.Boolean

    @Specialization(guards = ["isIntKind()"])
    protected fun writeInt(virtualFrame: VirtualFrame, value: Int): Int {
        virtualFrame.setInt(slot, value)
        return value
    }

    @Specialization(guards = ["isBooleanKind()"])
    protected fun writeBoolean(virtualFrame: VirtualFrame, value: Boolean): Boolean {
        virtualFrame.setBoolean(slot, value)
        return value
    }

    @Specialization(replaces = ["writeInt", "writeBoolean"])
    protected fun writeObject(virtualFrame: VirtualFrame, value: Any): Any {
        val slot = slot
        if (slot.kind != FrameSlotKind.Object) {
            CompilerDirectives.transferToInterpreterAndInvalidate()
            slot.kind = FrameSlotKind.Object
        }
        virtualFrame.setObject(slot, value)
        return value
    }
}

