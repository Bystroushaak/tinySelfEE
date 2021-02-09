package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.BareObject;

public class PrimitiveNil extends BareObject {
    private static PrimitiveNil instance = null;

    private PrimitiveNil() {

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
