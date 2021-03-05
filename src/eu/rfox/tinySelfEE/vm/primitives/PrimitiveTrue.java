package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicVisitor;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicallyVisitable;

public class PrimitiveTrue extends ObjectRepr implements SymbolicallyVisitable {
    private static PrimitiveTrue instance = null;

    private PrimitiveTrue() {

    }

    public static PrimitiveTrue getInstance() {
        if (instance == null) {
            instance = new PrimitiveTrue();
        }

        return instance;
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitPrimitiveTrue(this);
    }

    public String toString() {
        return "true";
    }
}
