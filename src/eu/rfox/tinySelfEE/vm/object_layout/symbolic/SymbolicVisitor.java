package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import eu.rfox.tinySelfEE.vm.primitives.*;

public interface SymbolicVisitor<R> {
    R visitPrimitiveTrue(PrimitiveTrue primitive_true);
    R visitPrimitiveStr(PrimitiveStr primitive_str);
    R visitPrimitiveNil(PrimitiveNil primitive_nil);
    R visitPrimitiveInt(PrimitiveInt primitive_int);
    R visitPrimitiveFloat(PrimitiveFloat primitive_float);
    R visitSymbolicReturn(SymbolicReturn symbolic_return);
    R visitSymbolicSend(SymbolicSend symbolic_send);
    R visitSymbolicMessage(SymbolicMessage symbolicMessage);
    R visitSymbolicObject(SymbolicObject symbolicObject);
}
