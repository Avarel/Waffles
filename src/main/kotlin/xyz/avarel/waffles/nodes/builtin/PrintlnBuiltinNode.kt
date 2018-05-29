package xyz.avarel.waffles.nodes.builtin

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.Specialization
import com.oracle.truffle.api.nodes.NodeInfo
import xyz.avarel.waffles.Tuple
import xyz.avarel.waffles.nodes.BuiltinNode

@GenerateNodeFactory
@NodeInfo(shortName = "wafflePrintln")
abstract class PrintlnBuiltinNode : BuiltinNode() {
    @Specialization
    fun wafflePrintln(value: Int): Tuple {
        println(value)
        return Tuple.EMPTY
    }

    @Specialization
    fun wafflePrintln(value: Boolean): Tuple {
        println(value)
        return Tuple.EMPTY
    }

    @Specialization
    fun wafflePrintln(value: Any): Any {
        println(value)
        return Tuple.EMPTY
    }
}
