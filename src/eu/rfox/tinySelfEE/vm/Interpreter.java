package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.bytecodes.Bytecode;
import eu.rfox.tinySelfEE.vm.object_layout.BlockRepr;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveNil;

/*
    TODO:
        Implement:
            doSendUnary
            doSendBinary
            doSendKeyword
            doPushLiteralAssignment
            doAddSlot
            doAddParent
            doReturnTop
            doReturnBlock
 */
public class Interpreter {
    ObjectRepr global_namespace;
    Process process;
    boolean has_error = false;
    String error_msg;

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
        int code_length = instructions.length;

        int bytecode;
        int index;
        int number_of_arguments;

        if (process.getSelf() == null) {
            process.setSelf(global_namespace);
        }

        while (process.callstack.ip < code_length && ! this.has_error) {
            bytecode = instructions[process.callstack.ip++];
            index = instructions[process.callstack.ip++];
            number_of_arguments = instructions[process.callstack.ip++];

            // TODO: remove unused parameters from method that don't need them
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
                setErrorMsg("Unknown instruction type: " + Integer.toString(bytecode));
                break;
            }
        }
    }

    void setErrorMsg(String message) {
        has_error = true;
        this.error_msg = message;
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
        ObjectRepr obj = process.pop();
        String parent_name = code.strings[index];
        ObjectRepr parent = (ObjectRepr) obj.getParentByName(parent_name);

        if (parent == null) {
            setErrorMsg("Couldn't find parent called `%s`." + parent_name);
            return;
        }

        process.push(parent);
    }

    void doPushLiteralNil(int index, int number_of_arguments, Code code) {
        process.push(PrimitiveNil.getInstance());
    }

    void doPushLiteralAssignment(int index, int number_of_arguments, Code code) {

    }

    void doPushLiteralInt(int index, int number_of_arguments, Code code) {
        process.push(code.literals_str[index]);  // TODO: solve for mutable state by cloning
    }

    void doPushLiteralFloat(int index, int number_of_arguments, Code code) {
        process.push(code.literals_str[index]);  // TODO: solve for mutable state by cloning
    }

    void doPushLiteralString(int index, int number_of_arguments, Code code) {
        process.push(code.literals_str[index]);  // TODO: solve for mutable state by cloning
    }

    void doPushLiteralObj(int index, int number_of_arguments, Code code) {
        process.push((ObjectRepr) code.literals_obj[index].clone());
    }

    void doPushLiteralBlock(int index, int number_of_arguments, Code code) {
        BlockRepr block = (BlockRepr) code.literals_block[index].clone();
        block.surrounding_obj = process.getSelf();
        process.push(block);
    }

    void doAddSlot(int index, int number_of_arguments, Code code) {
        ObjectRepr self = process.getSelf();
        ObjectRepr slot = process.pop();
        String slot_name = code.strings[index];
        self.setSlot(slot_name, slot);
    }

    void doAddParent(int index, int number_of_arguments, Code code) {
        ObjectRepr self = process.getSelf();
        ObjectRepr parent = process.pop();
        String slot_name = code.strings[index];
        self.setParent(slot_name, parent);
    }

    void doReturnTop(int index, int number_of_arguments, Code code) {

    }

    void doReturnBlock(int index, int number_of_arguments, Code code) {

    }
}
