package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class PrimitiveFloat extends ObjectRepr {
    float value;

    public PrimitiveFloat(float f) {
        super();
        this.value = f;
    }

    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
}
