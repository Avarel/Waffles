package xyz.avarel.waffles.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import xyz.avarel.waffles.nodes.BuiltinNode;

@GenerateNodeFactory
@NodeInfo(shortName = "println")
public abstract class PrintlnBuiltinNode extends BuiltinNode {
    @Specialization
    public int println(int value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public boolean println(boolean value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public Object println(Object value) {
        System.out.println(value);
        return value;
    }
}
