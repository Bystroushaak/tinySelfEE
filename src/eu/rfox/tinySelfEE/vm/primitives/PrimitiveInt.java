package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.Process;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;


class PrimitiveIntAdd extends PrimitiveCode {
    public PrimitiveIntAdd() {
        super();
        this.setArguments(new String[]{"b"});
    }

    public ObjectRepr evaluate(Process process, PrimitiveInt self, PrimitiveInt[] others) {
        PrimitiveInt other = others[0];
        PrimitiveInt new_val = new PrimitiveInt(self.getValue() + other.getValue());
        return new_val;
    }

    public ObjectRepr evaluate(Process process, PrimitiveInt self, PrimitiveFloat[] others) {
        PrimitiveFloat other = others[0];
        PrimitiveFloat new_val = new PrimitiveFloat(self.getValue() + other.getValue());
        return new_val;
    }

    public ObjectRepr evaluate(Process process, ObjectRepr self, ObjectRepr[] others)
            throws InvalidParametersException {
        throw new InvalidParametersException("PrimitiveInt's + can only evaluate other ints and floats.");
    }
}


class PrimitiveIntPrint extends PrimitiveCode {
    public PrimitiveIntPrint() {
        super();
    }

    public ObjectRepr evaluate(Process process, PrimitiveInt self, ObjectRepr[] others) {
        System.out.print(self.getValue());
        return PrimitiveNil.getInstance();
    }

    @Override
    public ObjectRepr evaluate(Process process, ObjectRepr self, ObjectRepr[] others) throws InvalidParametersException {
        return this.evaluate(process, (PrimitiveInt) self, others);
    }
}


class PrimitiveIntTrait extends ObjectRepr {
    private static PrimitiveIntTrait instance = null;

    private PrimitiveIntTrait() {
        super();

        this.setSlot("+", new PrimitiveIntAdd());
        this.setSlot("print", new PrimitiveIntPrint());
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
