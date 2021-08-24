package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.bytecodes.Bytecode;
import eu.rfox.tinySelfEE.vm.object_layout.BlockRepr;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveFloat;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveInt;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveStr;

public class Code {
    public String[] strings;

    public PrimitiveInt[] literals_int;
    public PrimitiveFloat[] literals_float;
    public PrimitiveStr[] literals_str;
    public ObjectRepr[] literals_obj;
    public BlockRepr[] literals_block;

    public int[] instructions;

    public String disassemble() {
        String output = "code = (| \n";

        output += "strings = [\n";
        for (int i = 0; i < strings.length; i++) {
            output += "\t'" + strings[i] + "', # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "literals_int = [\n";
        for (int i = 0; literals_int != null && i < literals_int.length; i++) {
            output += "\t" + Integer.toString(literals_int[i].getValue()) + ", # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "literals_float = [\n";
        for (int i = 0; literals_float != null && i < literals_float.length; i++) {
            output += "\t" + Float.toString(literals_float[i].getValue()) + ", # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "literals_str = [\n";
        for (int i = 0; literals_str != null && i < literals_str.length; i++) {
            output += "\t'" + literals_str[i].getValue() + "', # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "literals_obj = [\n";
        for (int i = 0; literals_obj != null && i < literals_obj.length; i++) {
            output += "\t" + literals_obj[i].ast + ", # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "literals_block = [\n";
        for (int i = 0; literals_block != null && i < literals_block.length; i++) {
            output += "\t" + literals_block[i].toString() + ", # " + Integer.toString(i) + "\n";
        }
        output += "].\n\n";

        output += "instructions = [\n";
        output += "\t# [instruction, index, number_of_arguments]\n";
        for (int i = 0; i < instructions.length;) {
            int bytecode = instructions[i++];
            int index = instructions[i++];
            int number_of_arguments = instructions[i++];
            output += "\t[" + instructionToString(bytecode, index, number_of_arguments) + "],\n";
        }
        output += "].\n";

        return output + "|)\n";
    }

    public String instructionToString(int bytecode, int index, int number_of_arguments) {

        Bytecode[] bytecodes = Bytecode.values();
        // make sure that it is sorted
        for (Bytecode bc : Bytecode.values()) {
            bytecodes[bc.value] = bc;
        }

        Bytecode bc_enum = bytecodes[bytecode];

        String output = bc_enum.name() + ", " + Integer.toString(index) + ", " + Integer.toString(number_of_arguments);

        // TODO: add hint

        return output;
    }
}
