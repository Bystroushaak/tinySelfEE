package eu.rfox.tinySelfEE.vm;

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
        String output = "(\n";

        return output + ")\n";
    }
}
