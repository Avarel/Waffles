package xyz.avarel.waffles.nodes

import com.oracle.truffle.api.dsl.NodeChild

@NodeChild(value = "arguments", type = Array<WaffleNode>::class)
abstract class BuiltinNode : WaffleNode()
