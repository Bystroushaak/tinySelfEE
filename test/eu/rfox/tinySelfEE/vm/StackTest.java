package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;
import org.junit.Test;

import static org.junit.Assert.*;

public class StackTest {

    @Test
    public void pushPop() {
        Stack stack = new Stack();

        ObjectRepr o = new ObjectRepr();
        stack.push(o);

        assertSame(stack.pop(), o);
        assertNull(stack.pop());
    }

    @Test
    public void pushResize() {
        Stack stack = new Stack();

        int size = 25;
        ObjectRepr[] objects = new ObjectRepr[size];
        for (int i = 0; i < size; i++) {
            ObjectRepr o = new ObjectRepr();
            objects[i] = o;
            stack.push(o);
        }

        for (int i = size - 1; i >= 0; i--) {
            assertEquals(stack.pop(), objects[i]);
        }

        assertNull(stack.pop());
    }
}