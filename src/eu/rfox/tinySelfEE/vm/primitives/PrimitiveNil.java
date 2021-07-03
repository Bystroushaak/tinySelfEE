package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class PrimitiveNil extends ObjectRepr {
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
}
