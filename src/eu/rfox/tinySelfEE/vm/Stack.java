package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;


public class Stack {
    public Stack prev_stack;
    public ObjectRepr self;
    public int ip = 0;

    int size = 20;
    int stack_pointer = 0;

    ObjectRepr[] stack_array;

    public Stack() {
        stack_array = new ObjectRepr[size];
    }

    public void push(ObjectRepr o) {
        checkSizeAndResize();
        stack_array[stack_pointer++] = o;
    }

    public ObjectRepr pop() {
        if (stack_pointer < 1) {
            return null;
        }

        ObjectRepr item = stack_array[--stack_pointer];
        stack_array[stack_pointer] = null;

        return item;
    }

    void checkSizeAndResize() {
        if (stack_pointer < size) {
            return;
        }

        ObjectRepr[] new_array = new ObjectRepr[size * 2];
        System.arraycopy(this.stack_array, 0, new_array, 0, size);

        this.stack_array = new_array;
        size *= 2;
    }
}
