package eu.rfox.tinySelfEE.parser.ast;

import java.util.*;

public class Obj implements ASTItem {
    boolean was_in_parens = false;
    public HashMap<String, ASTItem> slots;
    public ArrayList<String> arguments;
    public ArrayList<ASTItem> code;
    public HashMap<String, ASTItem> parents;

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
            slots = new LinkedHashMap<>();
        }

        slots.put(name, value);
    }

    public void addParent(String name, ASTItem value) {
        if (parents == null) {
            parents = new LinkedHashMap<>();
        }

        parents.put(name, value);
    }

    public void addRWSlot(String name) {
        if (!name.endsWith(":")) {
            name = name + ":";
        }
        addSlot(name, new AssignmentPrimitive());
    }

    /**
     * Used to detect if the object is really object or just parens for priority.
     */
    public boolean isSingleExpression() {
        if (parents != null && !parents.isEmpty()) {
            return false;
        }

        if (slots != null && !slots.isEmpty()) {
            return false;
        }

        if (code == null || code.isEmpty()) {
            return false;
        }

        if (code.size() > 1) {
            return false;
        }

        return true;
    }

    public ASTItem getFirstExpression() {
        return code.get(0);
    }

    public <R> R accept(ASTVisitor<R> visitor) {
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

    protected String objTypeForToString() {
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

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
