package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicVisitor;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicallyVisitable;

public class PrimitiveInt extends ObjectRepr implements SymbolicallyVisitable {
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitPrimitiveInt(this);
    }
}
