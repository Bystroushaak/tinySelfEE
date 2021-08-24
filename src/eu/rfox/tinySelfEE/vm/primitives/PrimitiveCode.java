package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.Process;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public abstract class PrimitiveCode extends ObjectRepr {
    public boolean hasPrimitiveCode() {
        return true;
    }

    abstract public ObjectRepr evaluate(Process process, ObjectRepr self, ObjectRepr[] others)
            throws InvalidParametersException;
}
