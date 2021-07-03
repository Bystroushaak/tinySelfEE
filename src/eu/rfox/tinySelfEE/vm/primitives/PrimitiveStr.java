package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class PrimitiveStr extends ObjectRepr {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
