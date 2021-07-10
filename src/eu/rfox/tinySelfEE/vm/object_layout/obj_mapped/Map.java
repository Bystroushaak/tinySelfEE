package eu.rfox.tinySelfEE.vm.object_layout.obj_mapped;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;

public class Map {
    HashMap<String, Integer> slots;
    HashMap<String, Integer> parent_slots;
    ArrayList<String> arguments;
    public boolean used_in_multiple_objects = false;

    int version = 0;
    // CodeContext code_context;
    List<String> parameters;

    private void incVersion() {
        this.version++;
    }

    private void createSlotsIfNotExists() {
        if (this.slots == null) {
            this.slots = new LinkedHashMap<>();
        }
    }
    private void createParentSlotsIfNotExists() {
        if (this.parent_slots == null) {
            this.parent_slots = new LinkedHashMap<>();
        }
    }

    public boolean hasCode() {
        return false;  // TODO: implement
    }

    public boolean hasPrimitiveCode() {
        return false;  // TODO: implement
    }

    public void addSlot(String slot_name, int index) {
        createSlotsIfNotExists();
        this.slots.put(slot_name, index);
        incVersion();
    }

    public int getSlot(String slot_name) {
        return slots.getOrDefault(slot_name, -1);
    }

    public void removeSlot(String slot_name) {
        if (this.slots == null) {
            return;
        }

        this.slots.remove(slot_name);
        incVersion();
    }

    public void addParent(String parent_name, int index) {
        createParentSlotsIfNotExists();
        parent_slots.put(parent_name, index);
        incVersion();
    }

    public void removeParent(String parent_name) {
        if (this.parent_slots == null) {
            return;
        }

        parent_slots.remove(parent_name);
    }

    public void addArgument(String argument_name) {
        if (this.arguments == null) {
            this.arguments = new ArrayList<>();
        }

        this.arguments.add(argument_name);
    }

    public void addArguments(ArrayList<String> arguments) {
        if (this.arguments == null) {
            this.arguments = arguments;
            return;
        }

        this.arguments.addAll(arguments);
    }
}
