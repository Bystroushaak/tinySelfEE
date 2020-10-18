package eu.rfox.oplang.parser.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Obj implements ASTItem {
    HashMap<String, ASTItem> slots;
    ArrayList<ASTItem> parameters;  // TODO: maybe strings?
    ArrayList<ASTItem> code;
    HashMap<String, Obj> parents;

    public Obj() {
    }


    public void addCode(ASTItem expr) {
        if (code == null) {
            code = new ArrayList<>();
        }

        code.add(expr);
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
                Objects.equals(parameters, obj.parameters) &&
                Objects.equals(code, obj.code) &&
                Objects.equals(parents, obj.parents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slots, parameters, code, parents);
    }

    @Override
    public String toString() {
        return "Obj{" +
                "slots=" + slots +
                ", parameters=" + parameters +
                ", code=" + code +
                ", parents=" + parents +
                '}';
    }
}
