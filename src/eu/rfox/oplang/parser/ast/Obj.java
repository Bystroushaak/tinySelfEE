package eu.rfox.oplang.parser.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Obj implements ASTItem {
    HashMap<String, ASTItem> slots;
    ArrayList<String> arguments;
    ArrayList<ASTItem> code;
    HashMap<String, ASTItem> parents;

    public Obj() {
    }

    public void addCode(ASTItem expr) {
        if (code == null) {
            code = new ArrayList<>();
        }

        code.add(expr);
    }

    public void addArgument(String name) {
        if (arguments == null) {
            arguments = new ArrayList<>();
        }

        arguments.add(name);
    }

    public void addArguments(ArrayList<String> arguments) {
        if (this.arguments == null) {
            this.arguments = arguments;
            return;
        }

        this.arguments.addAll(arguments);
    }

    public void addSlot(String name) {
        addSlot(name, new Nil());
    }

    public void addSlot(String name, ASTItem value) {
        if (name.charAt(name.length() - 1) == '*') {
            addParent(name, value);
            return;
        }

        if (slots == null) {
            slots = new HashMap<>();
        }

        slots.put(name, value);
    }

    public void addParent(String name, ASTItem value) {
        if (parents == null) {
            parents = new HashMap<>();
        }

        parents.put(name, value);
    }

    public void addRWSlot(String name) {
        addSlot(name, new AssignmentPrimitive());
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitObj(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obj obj = (Obj) o;
        return Objects.equals(slots, obj.slots) &&
                Objects.equals(arguments, obj.arguments) &&
                Objects.equals(code, obj.code) &&
                Objects.equals(parents, obj.parents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slots, arguments, code, parents);
    }

    private String objTypeForToString() {
        return "Obj";
    }

    @Override
    public String toString() {
        return objTypeForToString() + "{" +
                "parents=" + parents +
                ", slots=" + slots +
                ", arguments=" + arguments +
                ", code=" + code +
                '}';
    }
}
