package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicVisitor;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicallyVisitable;

public class PrimitiveFloat extends ObjectRepr implements SymbolicallyVisitable {
    float value;

    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitPrimitiveFloat(this);
    }
}
