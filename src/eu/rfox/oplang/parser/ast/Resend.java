package eu.rfox.oplang.parser.ast;

public class Resend extends SendBase implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitResend(this);
    }
}
