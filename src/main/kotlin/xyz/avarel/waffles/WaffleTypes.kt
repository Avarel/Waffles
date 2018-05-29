package xyz.avarel.waffles

import com.oracle.truffle.api.dsl.TypeSystem

@TypeSystem(Int::class, Long::class, Boolean::class, String::class, Tuple::class, WaffleFunction::class)
abstract class WaffleTypes
