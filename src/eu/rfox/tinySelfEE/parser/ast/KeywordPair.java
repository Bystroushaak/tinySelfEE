package eu.rfox.tinySelfEE.parser.ast;

public class KeywordPair implements ASTItem {
    boolean was_in_parens = false;

    String message_name;
    ASTItem parameter;

    public KeywordPair(String message_name, ASTItem parameter) {
        this.message_name = message_name;
        this.parameter = parameter;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return null;
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
