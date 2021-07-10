package eu.rfox.tinySelfEE.vm.object_layout;

import eu.rfox.tinySelfEE.vm.object_layout.obj_bare.BareObject;

/**
 * This class exists to allow easy switching between object implementations,
 * as every object implementation uses same interface, it is possible to just
 * switch inheritance chain here and everything should work like a charm.
 */
public class ObjectRepr extends BareObject {
}
