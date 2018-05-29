package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.ExplodeLoop
import com.oracle.truffle.api.nodes.IndirectCallNode
import com.oracle.truffle.api.nodes.UnexpectedResultException
import xyz.avarel.waffles.WaffleFunction

class InvokeNode(
        @field:Child private var functionNode: WaffleNode,
        @field:Children private var argumentNodes: Array<WaffleNode>
) : WaffleNode() {
    @field:Child private var callNode: IndirectCallNode = Truffle.getRuntime().createIndirectCallNode()

    @ExplodeLoop
    override fun execute(virtualFrame: VirtualFrame): Any {
        val function = evaluateFunction(virtualFrame)
        CompilerAsserts.compilationConstant<Any>(argumentNodes.size)

        val argumentValues = arrayOfNulls<Any>(argumentNodes.size + 1)
        argumentValues[0] = function.lexicalScope
        for (i in argumentNodes.indices) {
            argumentValues[i + 1] = argumentNodes[i].execute(virtualFrame)
        }

        return callNode.call(function.callTarget, argumentValues)
    }

    private fun evaluateFunction(virtualFrame: VirtualFrame): WaffleFunction {
        try {
            return functionNode.executeFunction(virtualFrame)
        } catch (e: UnexpectedResultException) {
            throw UnsupportedSpecializationException(this, arrayOf(functionNode))
        }

    }
}
