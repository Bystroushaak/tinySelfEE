package eu.rfox.oplang.parser.ast;

public abstract class MessageBase implements ASTItem {
    String message_name;

    public MessageBase(String message_name) {
        this.message_name = message_name;
    }

    public boolean isResend() {
        return message_name.indexOf('.') > -1;
    }

    public String removeParentName() {
        int dot_index = message_name.indexOf('.');
        String parent_name = message_name.substring(0, dot_index);
        message_name = message_name.substring(dot_index + 1);

        return parent_name;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitMessageBase(this);
    }
}
