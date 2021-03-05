package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicVisitor;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicallyVisitable;

public class PrimitiveStr extends ObjectRepr implements SymbolicallyVisitable {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitPrimitiveStr(this);
    }
}
