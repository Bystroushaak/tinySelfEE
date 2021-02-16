package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicObject;

import java.util.*;

public class Obj implements ASTItem {
    boolean was_in_parens = false;
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

    public SymbolicObject toSymbolicLayout(SymbolicObject scope_parent) {
        SymbolicObject o = new SymbolicObject();
        return this.toSymbolicLayout(scope_parent, o);
    }

    public SymbolicObject toSymbolicLayout(SymbolicObject scope_parent, SymbolicObject o) {
        if (scope_parent != null) {
            o.setScopeParent(scope_parent);
        }

        if (this.slots != null) {
            for (Map.Entry<String, ASTItem> entry : slots.entrySet()) {
                Obj ast_obj = (Obj) entry.getValue();
                o.setSlot(entry.getKey(), ast_obj.toSymbolicLayout(o));
            }
        }

        if (this.parents != null) {
            for (Map.Entry<String, ASTItem> entry : parents.entrySet()) {
                Obj ast_parent = (Obj) entry.getValue();
                o.setParent(entry.getKey(), ast_parent.toSymbolicLayout(null));
            }
        }

        if (this.arguments != null) {
            o.addArguments(this.arguments);
        }

        if (this.code != null) {
            for (ASTItem message: this.code) {
                o.addMessage(((MessageBase) message).toSymbolicMessage());
            }
        }

        return o;
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
