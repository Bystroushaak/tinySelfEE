package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicVisitor;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicallyVisitable;

public class PrimitiveNil extends ObjectRepr implements SymbolicallyVisitable {
    private static PrimitiveNil instance = null;

    private PrimitiveNil() {
        super();
    }

    public static PrimitiveNil getInstance() {
        if (instance == null) {
            instance = new PrimitiveNil();
        }

        return instance;
    }

    public String toString() {
        return "nil";
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitPrimitiveNil(this);
    }
}
