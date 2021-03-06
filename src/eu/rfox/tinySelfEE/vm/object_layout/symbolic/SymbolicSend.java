package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

public class SymbolicSend implements SymbolicEvalProtocol, SymbolicallyVisitable {
    boolean send_to_self = false;
    SymbolicEvalProtocol receiver;
    SymbolicMessage msg;

    public SymbolicSend(SymbolicMessage msg) {
        this.msg = msg;
    }

    public SymbolicSend(SymbolicEvalProtocol receiver, SymbolicMessage msg) {
        this.receiver = receiver;
        this.send_to_self = true;
        this.msg = msg;
    }

    public boolean isSend_to_self() {
        return send_to_self;
    }

    public SymbolicEvalProtocol getReceiver() {
        return receiver;
    }

    public SymbolicMessage getMsg() {
        return msg;
    }

    @Override
    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitSymbolicSend(this);
    }

    public void evaluate(SymbolicObject namespace, SymbolicFrame frame) {

    }
}
