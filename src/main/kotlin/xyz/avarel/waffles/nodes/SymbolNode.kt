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
import xyz.avarel.waffles.lexicalScope
import xyz.avarel.waffles.nodes.read.LexicalReadNodeGen

@NodeField(name = "name", type = String::class)
abstract class SymbolNode : WaffleNode() {
    abstract val name: String

    private lateinit var slot: FrameSlot
//    abstract val slot: FrameSlot

    @field:CompilationFinal
    private lateinit var resolvedSlot: FrameSlot
    @field:CompilationFinal
    private var lookUpDepth: Int = 0

    @ExplodeLoop
    private fun <T> readUpStack(getter: (Frame, FrameSlot) -> T, frame: Frame): T {
        if (!::resolvedSlot.isInitialized) {
            CompilerDirectives.transferToInterpreterAndInvalidate()
            resolveSlot(frame)
        }

        var lookupFrame = frame
        for (i in 0 until lookUpDepth) {
            lookupFrame = lookupFrame.lexicalScope!!
        }
        val value = getter(lookupFrame, resolvedSlot)

        CompilerDirectives.transferToInterpreterAndInvalidate()
        this.replace(LexicalReadNodeGen.create(lookupFrame.materialize(), resolvedSlot))
        return value
    }

    private fun resolveSlot(frame: Frame) {
        var frame: Frame = frame
        var slot = if (::slot.isInitialized) slot else {
            val lazy = frame.frameDescriptor.findFrameSlot(name)
            this.slot = lazy
            lazy
        }
        var depth = 0
        val id = slot.identifier
        var value = frame.getValue(slot)
        while (value == null) {
            depth++
            frame = frame.lexicalScope ?: let {
                CompilerDirectives.transferToInterpreterAndInvalidate()
                throw RuntimeException("Unknown variable $id")
            }
            val desc = frame.frameDescriptor
            slot = desc.findFrameSlot(id)
            if (slot != null) {
                value = frame.getValue(slot)
            }
        }
        lookUpDepth = depth
        resolvedSlot = slot
    }
    /*
    TESTcom.oracle.truffle.api.impl.DefaultMaterializedFrame@5a42bbf4
println
com.oracle.truffle.api.frame.FrameSlotTypeException
Spec
1
     */

    @Specialization(rewriteOn = [FrameSlotTypeException::class])
    @Throws(FrameSlotTypeException::class)
    protected fun readInt(frame: VirtualFrame): Int {
//        println("try readInt")
        return try {
            this.readUpStack(Frame::getInt, frame).also {
                println("Spec")
            }
        } catch (e: Exception) {
            println(slot.identifier)
            e.printStackTrace(System.out)
            throw e
        }
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

//    @Specialization
    @Specialization
    protected fun read(virtualFrame: VirtualFrame): Any? {
        return try {
            this.readUpStack(Frame::getValue, virtualFrame)
        } catch (ignore: FrameSlotTypeException) {
            null
        }
    }
}
