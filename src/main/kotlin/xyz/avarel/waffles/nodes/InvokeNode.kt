package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.DirectCallNode
import com.oracle.truffle.api.nodes.ExplodeLoop
import com.oracle.truffle.api.nodes.UnexpectedResultException
import xyz.avarel.waffles.WaffleFunction

class InvokeNode(
        @field:Child private var functionNode: WaffleNode,
        @field:Children private var argumentNodes: Array<WaffleNode>
) : WaffleNode() {
    @field:Child private lateinit var callNode: DirectCallNode

    @ExplodeLoop
    override fun execute(virtualFrame: VirtualFrame): Any {
        val function = evaluateFunction(virtualFrame)
        CompilerAsserts.compilationConstant<Any>(argumentNodes.size)

//        println(virtualFrame.lexicalScope?.lexicalScope)

        val argumentValues = arrayOfNulls<Any>(argumentNodes.size + 1)
        argumentValues[0] = function.lexicalScope
        for (i in argumentNodes.indices) {
            argumentValues[i + 1] = argumentNodes[i].execute(virtualFrame)
        }

        if (!::callNode.isInitialized) {
            CompilerDirectives.transferToInterpreterAndInvalidate()
            callNode = this.insert(Truffle.getRuntime().createDirectCallNode(function.callTarget))
        }

        if (function.callTarget != this.callNode.callTarget) {
            CompilerDirectives.transferToInterpreterAndInvalidate()
            throw UnsupportedOperationException("need to implement a proper inline cache")
        }

        return callNode.call(argumentValues)
    }

    private fun evaluateFunction(virtualFrame: VirtualFrame): WaffleFunction {
        try {
            return functionNode.executeFunction(virtualFrame)
        } catch (e: UnexpectedResultException) {
            throw UnsupportedSpecializationException(this, arrayOf(functionNode))
        }

    }
}
