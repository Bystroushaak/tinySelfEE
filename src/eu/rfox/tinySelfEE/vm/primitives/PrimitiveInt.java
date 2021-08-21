package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.Process;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

class PrimitiveIntAdd extends ObjectRepr implements PrimitiveEval {
    public PrimitiveIntAdd() {
        super();
        this.setArguments(new String[]{"b"});
    }

    public ObjectRepr evaluate(Process process, PrimitiveInt self, PrimitiveInt other) {
        PrimitiveInt new_val = new PrimitiveInt(self.getValue() + other.getValue());
        return new_val;
    }

    public ObjectRepr evaluate(Process process, PrimitiveInt self, PrimitiveFloat other) {
        PrimitiveFloat new_val = new PrimitiveFloat(self.getValue() + other.getValue());
        return new_val;
    }

    public ObjectRepr evaluate(Process process, ObjectRepr self, ObjectRepr other)
            throws InvalidParametersException {
        throw new InvalidParametersException("PrimitiveInt's + can only evaluate other ints and floats.");
    }
}


class PrimitiveIntTrait extends ObjectRepr {
    private static PrimitiveIntTrait instance = null;

    private PrimitiveIntTrait() {
        super();

        this.setSlot("+", new PrimitiveIntAdd());
    }

    public static PrimitiveIntTrait getInstance() {
        if (instance == null) {
            instance = new PrimitiveIntTrait();
        }

        return instance;
    }
}

public class PrimitiveInt extends ObjectRepr {
    int value;

    public PrimitiveInt() {
        super();

        this.setParent("trait_int", PrimitiveIntTrait.getInstance());
    }

    public PrimitiveInt(int i) {
        this();
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveInt that = (PrimitiveInt) o;
        return value == that.value;
    }
}
