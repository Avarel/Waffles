package xyz.avarel.waffles

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.frame.Frame
import com.oracle.truffle.api.frame.FrameDescriptor

/**
 * The first argument of the stack frame should be the parent frame (or null if it is the global frame).
 */
val Frame.lexicalScope get() = arguments[0] as Frame?

fun Frame.subFrame() = Truffle.getRuntime().createVirtualFrame(arrayOf(this), FrameDescriptor())