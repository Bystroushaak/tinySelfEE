package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class Interpreter {
    ObjectRepr global_namespace;

    Interpreter() {
        global_namespace = initGlobalNamespace();
    }

    ObjectRepr initGlobalNamespace() {
        ObjectRepr gns = new ObjectRepr();

        return gns;
    }

    public void addProcess() {

    }

    public void run() {

    }
}
