package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.bytecodes.Bytecode;
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

    public void addProcess(Process p) {
        process = p;
    }

    public void run() {
        Code code = process.code;
        int[] instructions = code.instructions;
        int ip = 0;

        int bytecode;
        int index;
        int number_of_arguments;

        if (process.getSelf() == null) {
            process.setSelf(global_namespace);
        }

        while (true) {
            bytecode = instructions[ip++];
            index = instructions[ip++];
            number_of_arguments = instructions[ip++];

            if (bytecode == Bytecode.NOP.value) {
                continue;
            } else if (bytecode == Bytecode.SEND_UNARY.value) {
                doSendUnary(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.SEND_BINARY.value) {
                doSendBinary(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.SEND_KEYWORD.value) {
                doSendKeyword(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_SELF.value) {
                doPushSelf(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_PARENT.value) {
                doPushParent(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_NIL.value) {
                doPushLiteralNil(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_ASSIGNMENT.value) {
                doPushLiteralAssignment(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_INT.value) {
                doPushLiteralInt(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_FLOAT.value) {
                doPushLiteralFloat(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_STRING.value) {
                doPushLiteralString(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_OBJ.value) {
                doPushLiteralObj(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.PUSH_LITERAL_BLOCK.value) {
                doPushLiteralBlock(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.ADD_SLOT.value) {
                doAddSlot(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.ADD_PARENT.value) {
                doAddParent(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.RETURN_TOP.value) {
                doReturnTop(index, number_of_arguments, code);
            } else if (bytecode == Bytecode.RETURN_BLOCK.value) {
                doReturnBlock(index, number_of_arguments, code);
            } else {
                // TODO: unknown instruction
            }
        }

    }

    void doSendUnary(int index, int number_of_arguments, Code code) {

    }

    void doSendBinary(int index, int number_of_arguments, Code code) {

    }

    void doSendKeyword(int index, int number_of_arguments, Code code) {

    }

    void doPushSelf(int index, int number_of_arguments, Code code) {
        process.pushSelf();
    }

    void doPushParent(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralNil(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralAssignment(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralInt(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralFloat(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralString(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralObj(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralBlock(int index, int number_of_arguments, Code code) {

    }

    void doAddSlot(int index, int number_of_arguments, Code code) {

    }

    void doAddParent(int index, int number_of_arguments, Code code) {

    }

    void doReturnTop(int index, int number_of_arguments, Code code) {

    }

    void doReturnBlock(int index, int number_of_arguments, Code code) {

    }
}
