package eu.rfox.oplang.parser.ast;

public class Resend implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitResend(this);
    }
}
