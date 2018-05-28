package xyz.avarel.waffles.nodes;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments", type = WaffleNode[].class)
public abstract class BuiltinNode extends WaffleNode {
}
