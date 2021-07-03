package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import eu.rfox.tinySelfEE.vm.object_layout.SlotNotFoundException;

public class SymbolicSend implements SymbolicEvalProtocol, SymbolicallyVisitable {
    boolean send_to_self = true;
    SymbolicEvalProtocol receiver;
    SymbolicMessage msg;

    public SymbolicSend(SymbolicMessage msg) {
        this.msg = msg;
    }

    public SymbolicSend(SymbolicEvalProtocol receiver, SymbolicMessage msg) {
        this.receiver = receiver;
        this.send_to_self = false;
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
        if (send_to_self) {
            frame.pushSelf();
        } else {
            receiver.evaluate(namespace, frame);
        }

        ObjectRepr top_obj = frame.pop();

        ObjectRepr slot;
        try {
            slot = (ObjectRepr) top_obj.slotLookup(msg.getMessage());
        } catch (SlotNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (slot.hasCode()) {

        }

    }
}
