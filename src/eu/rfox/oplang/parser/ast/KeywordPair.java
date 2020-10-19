package eu.rfox.oplang.parser.ast;

public class KeywordPair implements ASTItem {
    String message_name;
    ASTItem parameter;

    public KeywordPair(String message_name, ASTItem parameter) {
        this.message_name = message_name;
        this.parameter = parameter;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return null;
    }
}
