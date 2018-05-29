package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.dsl.NodeField
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.frame.Frame
import com.oracle.truffle.api.frame.FrameSlot
import com.oracle.truffle.api.frame.FrameSlotTypeException
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.ExplodeLoop
import xyz.avarel.waffles.nodes.read.LexicalReadNodeGen

@NodeField(name = "slot", type = FrameSlot::class)
abstract class SymbolNode : WaffleNode() {
    abstract val slot: FrameSlot

    @field:CompilationFinal
    private lateinit var resolvedSlot: FrameSlot
    @field:CompilationFinal
    private var lookUpDepth: Int = 0

    @ExplodeLoop
    private fun <T> readUpStack(getter: (Frame, FrameSlot) -> T, frame: Frame): T {
        if (!::resolvedSlot.isInitialized) {
            CompilerDirectives.transferToInterpreterAndInvalidate()
            resolveSlot(getter, frame)
        }

        var lookupFrame: Frame? = frame
        for (i in 0 until lookUpDepth) {
            lookupFrame = getLexicalScope(lookupFrame!!)
        }
        val value = getter(lookupFrame!!, resolvedSlot)

        CompilerDirectives.transferToInterpreterAndInvalidate()
        replace(LexicalReadNodeGen.create(lookupFrame.materialize(), resolvedSlot))
        return value
    }

    private fun <T> resolveSlot(getter: (Frame, FrameSlot) -> T, frame: Frame) {
        var frame: Frame? = frame
        var slot = slot
        var depth = 0
        val id = slot.identifier
        var value = getter(frame!!, slot)
        while (value == null) {
            depth++
            frame = getLexicalScope(frame!!)
            if (frame == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate()
                throw RuntimeException("Unknown variable $id")
            }
            val desc = frame.frameDescriptor
            slot = desc.findFrameSlot(id)
            if (slot != null) {
                value = getter(frame, slot)
            }
        }
        lookUpDepth = depth
        resolvedSlot = slot
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
