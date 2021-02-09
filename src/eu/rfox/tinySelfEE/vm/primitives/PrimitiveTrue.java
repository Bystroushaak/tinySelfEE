package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.BareObject;

public class PrimitiveTrue extends BareObject {
    private static PrimitiveTrue instance = null;

    private PrimitiveTrue() {

    }

    public static PrimitiveTrue getInstance() {
        if (instance == null) {
            instance = new PrimitiveTrue();
        }

        return instance;
    }

    public String toString() {
        return "true";
    }
}
