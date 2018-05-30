package xyz.avarel.waffles.nodes.read

import com.oracle.truffle.api.dsl.NodeField
import com.oracle.truffle.api.dsl.NodeFields
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.FrameSlotTypeException
import com.oracle.truffle.api.frame.MaterializedFrame
import com.oracle.truffle.api.frame.VirtualFrame
import xyz.avarel.waffles.nodes.WaffleNode

@NodeFields(
        NodeField(name = "scope", type = MaterializedFrame::class),
        NodeField(name = "slot", type = FrameSlot::class)
)
abstract class LexicalReadNode: WaffleNode() {
    protected abstract val scope: MaterializedFrame
    protected abstract val slot: FrameSlot

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readInt(virtualFrame: VirtualFrame): Int {
        val int = scope.getInt(slot)
        return int
    }

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readBoolean(virtualFrame: VirtualFrame) = scope.getBoolean(slot)

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readObject(virtualFrame: VirtualFrame) = scope.getObject(slot)

    @Specialization(replaces = ["readInt", "readBoolean", "readObject"])
    fun read(virtualFrame: VirtualFrame): Any = scope.getValue(slot)
}