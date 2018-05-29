package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.dsl.NodeField
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.frame.Frame
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.FrameSlotTypeException
import com.oracle.truffle.api.frame.VirtualFrame

@NodeField(name = "slot", type = FrameSlot::class)
abstract class SymbolNode : WaffleNode() {
    abstract val slot: FrameSlot

    private inline fun <T> readUpStack(getter: (Frame, FrameSlot) -> T, frame: Frame?): T {
        var frame = frame
        val slot = slot
        var value: T? = getter(frame!!, slot)
        while (value == null) {
            frame = getLexicalScope(frame!!)
            if (frame == null) {
                throw RuntimeException("Unknown variable: " + slot.identifier)
            }
            value = getter(frame, slot)
        }
        return value
    }

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readInt(frame: VirtualFrame): Long {
        return this.readUpStack(Frame::getInt, frame).toLong()
    }

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readBoolean(frame: VirtualFrame): Boolean {
        return this.readUpStack(Frame::getBoolean, frame)
    }

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readObject(frame: VirtualFrame): Any {
        return this.readUpStack(Frame::getObject, frame)
    }

    @Specialization(replaces = ["readInt", "readBoolean", "readObject"])
    protected fun read(virtualFrame: VirtualFrame): Any? {
        return try {
            this.readUpStack(Frame::getValue, virtualFrame)
        } catch (ignore: FrameSlotTypeException) {
            null
        }

    }

    private fun getLexicalScope(frame: Frame): Frame? {
        return frame.arguments[0] as Frame?
    }
}
