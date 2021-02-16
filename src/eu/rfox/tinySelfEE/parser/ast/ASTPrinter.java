package eu.rfox.tinySelfEE.parser.ast;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ASTPrinter implements ASTVisitor<String> {
    private int indentation = 0;

    public String print(ASTItem expr) {
        return expr.accept(this);
    }

    private String getIndent() {
        String out = "";
        for (int i = 0; i < indentation; i++) {
            out += "  ";
        }
        return out;
    }

    private String printParens(String out, ASTItem item) {
        if (item.wasInParens()) {
            return "(" + out + ")";
        }

        return out;
    }

    @Override
    public String visitAssignmentPrimitive(AssignmentPrimitive expr) {
        return "AssignmentPrimitive";
    }

    @Override
    public String visitStr(Str string) {
        return "\"" + string.value + "\"";  // TODO: escaping
    }

    @Override
    public String visitSend(Send send) {
        String out = "";

        if (!send.hasDefaultSelf()) {
            out = send.obj.accept(this) + " ";
        }

        return printParens(out + send.message.accept(this), send);
    }

    @Override
    public String visitSelf(Self self) {
        return "self";
    }

    @Override
    public String visitRoot(Root root) {
        return "Root";
    }

    @Override
    public String visitNumberFloat(NumberFloat numberFloat) {
        return Float.toString(numberFloat.value);
    }

    @Override
    public String visitNumberInt(NumberInt numberInt) {
        return Integer.toString(numberInt.value);
    }

    @Override
    public String visitObj(Obj object) {
        return formatObjOrBlock(object, true);
    }

    @Override
    public String visitBlock(Block block) {
        return formatObjOrBlock(block, false);
    }

    private String formatObjOrBlock(Obj object, boolean format_as_obj) {
        String out;
        if (format_as_obj) {
            out = "(";
        } else {
            out = "[";
        }

        if (object.isSingleExpression()) {
            if (object.code != null) {
                out += object.code.get(0).accept(this);
            }

            return out + (format_as_obj ? ")" : "]");
        }

        indentation++;

        if (object.parents != null || object.slots != null || object.arguments != null) {
            out += "|";
        }

        if (object.parents != null) {
            out += "\n";
            for (Map.Entry<String, ASTItem> entry : object.parents.entrySet()) {
                out += getIndent();
                out += entry.getKey();
                out += " = ";
                out += entry.getValue().accept(this);
                out += ".\n";
            }
        }

        if (object.arguments != null) {
            out += "\n";
            for (String argument : object.arguments) {
                out += getIndent() + ":" + argument + ".\n";
            }
        }

        if (object.slots != null) {
            out += "\n";
            for (Map.Entry<String, ASTItem> entry : object.slots.entrySet()) {
                out += getIndent();
                out += entry.getKey();

                if (!entry.getValue().accept(this).equals("nil")) {
                    out += " = ";
                    out += entry.getValue().accept(this);
                }
                out += ".\n";
            }
        }

        indentation--;
        if (object.parents != null || object.slots != null || object.arguments != null) {
            out += getIndent() + "|";
        }

        if (object.code != null) {
            indentation++;
            out += "\n";
            for (ASTItem line : object.code) {
                out += getIndent();
                out += line.accept(this);
                out += ".\n";
            }
            indentation--;
            out += getIndent();
        }


        if (format_as_obj) {
            return out + ")";
        }

        return out + "]";
    }

    @Override
    public String visitResend(Resend resend) {
        return printParens(resend.parent_name + "." + resend.message.message_name, resend);
    }

    @Override
    public String visitReturn(Return aReturn) {
        return printParens("^ " + aReturn.value.accept(this), aReturn);
    }

    @Override
    public String visitNil(Nil nil) {
        return "nil";
    }

    @Override
    public String visitMessageUnary(MessageUnary messageUnary) {
        return printParens(messageUnary.message_name, messageUnary);
    }

    @Override
    public String visitMessageBinary(MessageBinary messageBinary) {
        return printParens(messageBinary.message_name + " " + messageBinary.parameter.accept(this),
                           messageBinary);
    }

    @Override
    public String visitMessageKeyword(MessageKeyword messageKeyword) {
        String out = "";
        String[] message_segments = messageKeyword.message_name.split(":");

        for (int i = 0; i < message_segments.length; i++) {
            out += message_segments[i] + ": " + messageKeyword.parameters.get(i).accept(this) + " ";
        }

        return printParens(out.substring(0, out.length() - 1), messageKeyword);
    }

    @Override
    public String visitMessageBase(MessageBase messageBase) {
        return "MessageBase";
    }

    @Override
    public String visitCascade(Cascade cascade) {
        String out = cascade.obj.accept(this) + " ";

        List<String> messages = cascade.messages.stream().map(v -> v.accept(this)).collect(Collectors.toList());

        return printParens(out + String.join(";\n" + getIndent(), messages), cascade);
    }
}
