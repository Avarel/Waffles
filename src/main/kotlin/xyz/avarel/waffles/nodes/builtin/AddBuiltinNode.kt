package xyz.avarel.waffles.nodes.builtin

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.nodes.NodeInfo
import xyz.avarel.waffles.nodes.BuiltinNode

@GenerateNodeFactory
@NodeInfo(shortName = "+")
abstract class AddBuiltinNode : BuiltinNode() {
    @Specialization
    fun add(value0: Int, value1: Int) = value0 + value1
}
