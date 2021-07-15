package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.BlockRepr;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class Code {
    public String[] strings;

    public Integer[] literals_int;
    public Float[] literals_float;
    public String[] literals_str;
    public ObjectRepr[] literals_obj;
    public BlockRepr[] literals_block;

    public int[] instructions;

    public String disassemble() {
        String output = "(\n";

        return output + ")\n";
    }
}
