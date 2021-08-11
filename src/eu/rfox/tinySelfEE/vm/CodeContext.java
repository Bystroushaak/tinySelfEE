package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.bytecodes.Bytecode;
import eu.rfox.tinySelfEE.vm.object_layout.BlockRepr;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import eu.rfox.tinySelfEE.vm.primitives.PrimitiveInt;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveFloat;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveStr;

import java.util.ArrayList;

class Instruction {
    public int bytecode = 0;
    public int index = 0;
    public int number_of_arguments = Bytecode.NOP.value;

    Instruction(Bytecode bytecode) {
        this.bytecode = bytecode.value;
    }

    Instruction(Bytecode bytecode, int index) {
        this.bytecode = bytecode.value;
        this.index = index;
    }

    Instruction(Bytecode bytecode, int index, int number_of_arguments) {
        this.bytecode = bytecode.value;
        this.index = index;
        this.number_of_arguments = number_of_arguments;
    }

    public int size() {
        return 3;
    }

    public int saveCode(int[] code_array, int instruction_pointer) {
        code_array[instruction_pointer++] = bytecode;
        code_array[instruction_pointer++] = index;
        code_array[instruction_pointer++] = number_of_arguments;

        return instruction_pointer;
    }
}

public class CodeContext {
    /*
    TODO: rework so that primitive objects are directly created - it will save time later and
          they can be cloned efficiently

    TODO: is will_have_slots really used? if not, remove it
    */
    public boolean will_have_slots = false;

    private ArrayList<String> strings;

    private ArrayList<PrimitiveInt> literals_int;
    private ArrayList<PrimitiveFloat> literals_float;
    private ArrayList<PrimitiveStr> literals_str;
    private ArrayList<ObjectRepr> literals_obj;
    private ArrayList<BlockRepr> literals_block;

    private ArrayList<Instruction> instructions;

    public CodeContext() {
        instructions = new ArrayList<>();
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    // Strings /////////////////////////////////////////////////////////////////////////////////////////////////////////
    int addString(String s) {
        if (strings == null) {
            strings = new ArrayList<>();
        }

        strings.add(s);

        return (strings.size() - 1);
    }

    // Add literal & return index section //////////////////////////////////////////////////////////////////////////////
    int addIntLiteral(int i) {
        if (literals_int == null) {
            literals_int = new ArrayList<>();
        }

        literals_int.add(new PrimitiveInt(i));

        return (literals_int.size() - 1);
    }

    int addFloatLiteral(float f) {
        if (literals_float == null) {
            literals_float = new ArrayList<PrimitiveFloat>();
        }

        literals_float.add(new PrimitiveFloat(f));

        return literals_float.size() - 1;
    }

    int addStringLiteral(String s) {
        if (literals_str == null) {
            literals_str = new ArrayList<>();
        }

        literals_str.add(new PrimitiveStr(s));

        return literals_str.size() - 1;
    }

    int addObjectLiteral(ObjectRepr o) {
        if (literals_obj == null) {
            literals_obj = new ArrayList<>();
        }

        literals_obj.add(o);

        return literals_obj.size() - 1;
    }

    int addBlockLiteral(BlockRepr b) {
        if (literals_block == null) {
            literals_block = new ArrayList<>();
        }

        literals_block.add(b);

        return literals_block.size() - 1;
    }

    // Add literal & bytecode section //////////////////////////////////////////////////////////////////////////////////
    public void addNilLiteralBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_NIL));
    }

    public void addAssingmentLiteralBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_ASSIGNMENT));
    }

    public void addIntLiteralAndBytecode(int i) {
        int index = addIntLiteral(i);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_INT, index));
    }

    public void addFloatLiteralAndBytecode(float f) {
        int index = addFloatLiteral(f);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_FLOAT, index));
    }

    public void addStringLiteralAndBytecode(String s) {
        int index = addStringLiteral(s);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_STRING, index));
    }

    public void addObjectLiteralAndBytecode(ObjectRepr o) {
        int index = addObjectLiteral(o);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_OBJ, index));
    }

    public void addBlockLiteralAndBytecode(BlockRepr o) {
        int index = addBlockLiteral(o);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL_BLOCK, index));
    }

    // Other bytecodes /////////////////////////////////////////////////////////////////////////////////////////////////
    public void addPushSelfBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_SELF));
    }

    public void addPushParentBytecode(String parent_name) {
        int index;
        if (strings == null) {
            index = addString(parent_name);
        } else {
            int item_index = strings.indexOf(parent_name);

            if (item_index == -1) {
                index = addString(parent_name);
            } else {
                index = item_index;
            }
        }

        addInstruction(new Instruction(Bytecode.PUSH_PARENT, index));
    }

    public void addUnaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND_UNARY, index));
    }

    public void addBinaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND_BINARY, index, 1));
    }

    public void addKeywordMessageSendBytecode(String message_name, int number_of_arguments) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND_KEYWORD, index, number_of_arguments));
    }

    public void addReturnTopBytecode() {
        addInstruction(new Instruction(Bytecode.RETURN_TOP));
    }

    public void addReturnBlockBytecode() {
        addInstruction(new Instruction(Bytecode.RETURN_BLOCK));
    }

    public void addSlotBytecode(String slot_name) {
        int index = addString(slot_name);
        addInstruction(new Instruction(Bytecode.ADD_SLOT, index));
    }

    public void addParentBytecode(String parent_name) {
        int index = addString(parent_name);
        addInstruction(new Instruction(Bytecode.ADD_PARENT, index));
    }

    /**
     * Compile the relatively inefficient code to more compact data structures.
     */
    public Code compile() {
        Code code = new Code();

        if (strings != null) {
            code.strings = new String[strings.size()];
            this.strings.toArray(code.strings);
        }

        if (literals_int != null) {
            code.literals_int = new PrimitiveInt[literals_int.size()];
            this.literals_int.toArray(code.literals_int);
        }

        if (literals_float != null) {
            code.literals_float = new PrimitiveFloat[literals_float.size()];
            this.literals_float.toArray(code.literals_float);
        }

        if (literals_str != null) {
            code.literals_str = new PrimitiveStr[literals_str.size()];
            this.literals_str.toArray(code.literals_str);
        }

        if (literals_obj != null) {
            code.literals_obj = new ObjectRepr[literals_obj.size()];
            this.literals_obj.toArray(code.literals_obj);
        }

        if (literals_block != null) {
            code.literals_block = new BlockRepr[literals_block.size()];
            this.literals_block.toArray(code.literals_block);
        }

        int size = 0;
        for (Instruction instruction : instructions) {
            size += instruction.size();
        }
        code.instructions = new int[size];
        int instructions_pointer = 0;
        for (Instruction instruction : instructions) {
            instructions_pointer = instruction.saveCode(code.instructions, instructions_pointer);
        }

        if (literals_obj != null) {
            for (ObjectRepr obj : literals_obj) {
                if (obj.code != null) {
                    obj.code = obj.code_context.compile();
                    obj.code_context = null;
                }
            }
        }

        if (literals_block != null) {
            for (BlockRepr block : literals_block) {
                block.code = block.code_context.compile();
                block.code_context = null;
            }
        }

        return code;
    }
}
