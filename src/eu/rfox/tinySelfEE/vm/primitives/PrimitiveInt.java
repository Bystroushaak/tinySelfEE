package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class PrimitiveInt extends ObjectRepr {
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
