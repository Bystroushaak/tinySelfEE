package eu.rfox.oplang.parser.ast;

import java.util.ArrayList;

public class Root implements ASTItem {
    ArrayList<ASTItem> ast;

    Root() {
        ast = new ArrayList<ASTItem>();
    }

    Root(ASTItem item) {
        ast.add(item);
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitRoot(this);
    }
}
