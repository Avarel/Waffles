package xyz.avarel.waffles.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import xyz.avarel.waffles.nodes.BuiltinNode;

@GenerateNodeFactory
@NodeInfo(shortName = "+")
public abstract class AddBuiltinNode extends BuiltinNode {
    @Specialization
    public int add(int value0, int value1) {
        return value0 + value1;
    }
}
