package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.Process;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public interface PrimitiveEval {
    ObjectRepr evaluate(Process process, ObjectRepr self, ObjectRepr other)
            throws InvalidParametersException;
}
