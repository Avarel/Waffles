package xyz.avarel.waffles;

import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({int.class, boolean.class, WaffleFunction.class})
public abstract class WaffleTypes {
}
