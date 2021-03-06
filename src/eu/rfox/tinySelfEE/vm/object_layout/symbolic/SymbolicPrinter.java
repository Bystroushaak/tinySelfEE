package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectInterface;
import eu.rfox.tinySelfEE.vm.primitives.*;

import java.util.Map;

public class SymbolicPrinter implements SymbolicVisitor<String> {
    private int indentation = 0;

    public String print(SymbolicallyVisitable expr) {
        return expr.accept(this);
    }

    private String getIndent() {
        String out = "";
        for (int i = 0; i < indentation; i++) {
            out += "  ";
        }
        return out;
    }

    @Override
    public String visitPrimitiveTrue(PrimitiveTrue primitive_true) {
        return getIndent() + "true";
    }

    @Override
    public String visitPrimitiveStr(PrimitiveStr primitive_str) {
        return getIndent() + "\"" + primitive_str.getValue() + "\"";
    }

    @Override
    public String visitPrimitiveNil(PrimitiveNil primitive_nil) {
        return getIndent() + "nil";
    }

    @Override
    public String visitPrimitiveInt(PrimitiveInt primitive_int) {
        return getIndent() + String.valueOf(primitive_int.getValue());
    }

    @Override
    public String visitPrimitiveFloat(PrimitiveFloat primitive_float) {
        return getIndent() + String.valueOf(primitive_float.getValue());
    }

    @Override
    public String visitSymbolicSend(SymbolicSend symbolic_send) {
        String result = getIndent() + "SymbolicSend(\n";
        indentation++;

        SymbolicEvalProtocol receiver = symbolic_send.getReceiver();
        if (receiver == null) {
            result += getIndent() + "(default) self,\n";
        } else {
            result += ((SymbolicallyVisitable) receiver).accept(this) + "\n";
        }
        result += symbolic_send.getMsg().accept(this);;

        indentation--;
        result += getIndent() + "),\n";

        return result;
    }

    @Override
    public String visitSymbolicMessage(SymbolicMessage symbolicMessage) {
        String result = getIndent() + "SymbolicMessage(\n";
        indentation++;

        result += getIndent() + "message: \"" + symbolicMessage.getMessage() + "\",\n";

        if (symbolicMessage.hasArguments()) {
            result += getIndent() + "arguments = [\n";

            indentation++;
            for (SymbolicEvalProtocol item : symbolicMessage.getArguments()) {
                result += ((SymbolicallyVisitable) item).accept(this) + ",\n";
            }
            indentation--;

            result += getIndent() + "],\n";
        }

        indentation--;
        result += getIndent() + "),\n";

        return result;
    }

    @Override
    public String visitSymbolicObject(SymbolicObject symbolic_object) {
        String result = getIndent() + "SymbolicObject(\n";

        indentation++;

        result += getIndent() + "id: " + String.valueOf(symbolic_object.getId()) + ",\n";
        result += getIndent() + "version: " + String.valueOf(symbolic_object.getVersion()) + ",\n";

        if (symbolic_object.hasArguments()) {
            result += getIndent() + "arguments = [\n";

            indentation++;
            for (String argument_name : symbolic_object.getArguments()) {
                result += getIndent() + "\"" + argument_name + "\",\n";
            }
            indentation--;

            result += getIndent() + "],\n";
        }

        if (symbolic_object.hasSlots()) {
            result += getIndent() + "slots = [\n";
            indentation++;

            for (Map.Entry<String, ObjectInterface> entry : symbolic_object.getSlots().entrySet()) {
                result += getIndent() + "\"" + entry.getKey() + "\" = ";

                indentation++;
                String value = ((SymbolicallyVisitable) entry.getValue()).accept(this);
                result += value.trim() + ",\n";
                indentation--;
            }

            indentation--;
            result += getIndent() + "],\n";
        }

        if (symbolic_object.hasParents()) {
            result += getIndent() + "parent_slots = [\n";
            indentation++;

            for (Map.Entry<String, ObjectInterface> entry : symbolic_object.getParentSlots().entrySet()) {
                result += getIndent() + "\"" + entry.getKey() + "\" = ";

                indentation++;
                String value = ((SymbolicallyVisitable) entry.getValue()).accept(this);
                result += value.trim() + ",\n";
                indentation--;
            }

            indentation--;
            result += getIndent() + "],\n";
        }

        if (symbolic_object.hasCode()) {
            result += getIndent() + "code = [\n";

            indentation++;
            for (SymbolicEvalProtocol instruction : symbolic_object.getCode()) {
                result += ((SymbolicallyVisitable) instruction).accept(this);
            }
            indentation--;

            result += getIndent() + "],\n";
        }

        indentation--;
        result += getIndent() + ")";

        return result;
    }

    @Override
    public String visitSymbolicReturn(SymbolicReturn symbolic_return) {
        return "SymbolicReturn not yet implemented";
    }
}
