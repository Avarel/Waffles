package xyz.avarel.waffles.nodes.flow

import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.UnexpectedResultException
import com.oracle.truffle.api.profiles.ConditionProfile
import xyz.avarel.waffles.nodes.WaffleNode

class IfNode(
        @field:Child private var testNode: WaffleNode,
        @field:Child private var thenNode: WaffleNode,
        @field:Child private var elseNode: WaffleNode
) : WaffleNode() {
    private val conditionProfile = ConditionProfile.createBinaryProfile()

    override fun execute(virtualFrame: VirtualFrame): Any {
        return if (this.conditionProfile.profile(testResult(virtualFrame))) {
            thenNode.execute(virtualFrame)
        } else {
            elseNode.execute(virtualFrame)
        }
    }

    private fun testResult(virtualFrame: VirtualFrame): Boolean {
        return try {
            testNode.executeBoolean(virtualFrame)
        } catch (ignore: UnexpectedResultException) {
            false
        }

    }
}
