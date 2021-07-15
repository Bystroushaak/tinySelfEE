package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessTest {
    Process getProcess() {
        Code code = new Code();
        return new Process(code);
    }

    @Test
    public void pushPop() {
        Process p = getProcess();

        ObjectRepr obj = new ObjectRepr();
        p.push(obj);

        assertSame(p.pop(), obj);
        assertNull(p.pop());
    }

    @Test
    public void pushPopFrame() {
        Process p = getProcess();

        assertNull(p.pop());

        ObjectRepr first = new ObjectRepr();
        p.push(first);

        p.pushFrame();

        assertNull(p.pop());

        ObjectRepr second = new ObjectRepr();
        p.push(second);

        assertSame(p.pop(), second);
        assertNull(p.pop());

        p.popFrame();

        assertSame(p.pop(), first);
        assertNull(p.pop());
    }

    @Test
    public void popFrameDown() {
        Process p = getProcess();

        assertNull(p.pop());

        ObjectRepr first = new ObjectRepr();
        p.push(first);

        p.pushFrame();

        assertNull(p.pop());

        ObjectRepr second = new ObjectRepr();
        p.push(second);

        p.popFrameDown();

        assertSame(p.pop(), second);
        assertSame(p.pop(), first);
        assertNull(p.pop());
    }

    @Test
    public void setSelfGetSelf() {
        Process p = getProcess();

        assertNull(p.getSelf());

        ObjectRepr self = new ObjectRepr();

        p.setSelf(self);
        assertSame(p.getSelf(), self);
    }
}