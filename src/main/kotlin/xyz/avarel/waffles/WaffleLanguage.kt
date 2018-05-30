package xyz.avarel.waffles

import com.oracle.truffle.api.CallTarget
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleLanguage
import xyz.avarel.waffles.nodes.InvokeNode
import xyz.avarel.waffles.nodes.SymbolNodeGen
import xyz.avarel.waffles.nodes.WaffleEvalRootNode

@TruffleLanguage.Registration(
        name = "Waffles",
        version = "0.1",
        mimeType = ["application/x-waffles"]
)
class WaffleLanguage: TruffleLanguage<WaffleContext>() {
    override fun parse(request: ParsingRequest): CallTarget {
        val ctx = WaffleContext()
//        println(request.source.createSection(1))

//        request.source.reader

        val node = InvokeNode(
                SymbolNodeGen.create("println"),
                arrayOf(SymbolNodeGen.create("ONE"))
        )

//        println("???" + ctx.globalFrame.frameDescriptor.findFrameSlot("ONE"))
//        val node = SymbolNodeGen.create("ONE")

        val function = WaffleFunction.create(emptyArray(), arrayOf(node), ctx.globalFrameDescriptor)
//        function.lexicalScope = ctx.globalFrame
        //val dcn = Truffle.getRuntime().createDirectCallNode(function.callTarget)
        return Truffle.getRuntime().createCallTarget(WaffleEvalRootNode(this, function.callTarget))
    }

    override fun createContext(env: Env): WaffleContext = WaffleContext()

    override fun isObjectOfLanguage(obj: Any?): Boolean {
        TODO("not implemented")
    }
}

