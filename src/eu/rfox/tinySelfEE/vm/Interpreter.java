package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class Interpreter {
    ObjectRepr global_namespace;
    Process process;

    public Interpreter(Process p) {
        global_namespace = initGlobalNamespace();
        process = p;
    }

    ObjectRepr initGlobalNamespace() {
        ObjectRepr gns = new ObjectRepr();

        return gns;
    }

    public void run() {

    }

    public void addProcess(Process p) {
        process = p;
    }
}
