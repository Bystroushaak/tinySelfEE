package eu.rfox.tinySelfEE.vm.object_layout.obj_mapped;

import java.security.KeyException;
import java.util.ArrayList;

public class MappedObject {
    static long global_id = 0;

    long id;
    Map map;
    MappedObject scope_parent;
    boolean visited = false;

    ArrayList<MappedObject> parent_slot_values;
    ArrayList<MappedObject> slot_values;

    public MappedObject() {
        this(new Map());
    }

    public MappedObject(Map map) {
        this.map = map;
        this.id = MappedObject.global_id++;
    }

    public boolean hasCode() {
        return map.hasCode();
    }

    public boolean hasPrimitiveCode() {
        return map.hasPrimitiveCode();
    }

    public MappedObject clone() {
        MappedObject new_obj = new MappedObject(this.map);
        this.map.used_in_multiple_objects = true;

        if (this.parent_slot_values != null) {
            new_obj.parent_slot_values = new ArrayList<>(this.parent_slot_values);
        }

        if (this.slot_values != null) {
            new_obj.slot_values = new ArrayList<>(this.slot_values);
        }

        new_obj.scope_parent = this.scope_parent;

        return new_obj;
    }

    private void createSlotsIfNotExists() {
        if (this.slot_values == null) {
            this.slot_values = new ArrayList<MappedObject>();
        }
    }

    private void createParentSlotsIfNotExists() {
        if (this.parent_slot_values == null) {
            this.parent_slot_values = new ArrayList<MappedObject>();
        }
    }

    public void setSlot(String slot_name, MappedObject value) {
        int slot_index = map.getSlot(slot_name);

        if (slot_index == -1) {
            createSlotsIfNotExists();
            slot_values.add(value);
            map.addSlot(slot_name, slot_values.size() - 1);
            return;
        }

        slot_values.add(slot_index, value);
    }

    public void setParent(String parent_name, MappedObject value) {
        int slot_index = map.getSlot(parent_name);

        if (slot_index == -1) {
            createSlotsIfNotExists();
            parent_slot_values.add(value);
            map.addParent(parent_name, parent_slot_values.size() - 1);
            return;
        }

        parent_slot_values.add(slot_index, value);
    }

    public void setScopeParent(MappedObject scope_parent) {
        this.scope_parent = scope_parent;
    }

    public void addArgument(String argument_name) {
        this.map.addArgument(argument_name);
    }

    public void addArguments(ArrayList<String> arguments) {
        this.map.addArguments(arguments);
    }

    public MappedObject getLocalSlot(String slot_name) {
        int slot_index = map.getSlot(slot_name);

        if (slot_index == -1) {
            return null;
        }

        return slot_values.get(slot_index);
    }

    public MappedObject parentLookup(String slot_name) throws KeyException {
        ArrayList<MappedObject> parents = new ArrayList<>();
        parents.add(this);

        return lookForSlotInParentTree(parents, slot_name);
    }

    private MappedObject lookForSlotInParentTree(ArrayList<MappedObject> parents, String slot_name) throws KeyException {
        MappedObject result = null;

        ArrayList<MappedObject> visited_objects = new ArrayList<>();
        while (!parents.isEmpty()) {
            MappedObject obj = parents.remove(0);

            if (obj.visited) {
                continue;
            }

            obj.visited = true;
            visited_objects.add(obj);

            MappedObject slot = obj.getLocalSlot(slot_name);
            if (slot != null) {
                if (result != null) {
                    throw new KeyException("Too many parent slots `" + slot_name +"`, use resend!");
                }

                result = slot;
                continue;
            }

            if (obj.scope_parent != null) {
                parents.add(obj.scope_parent);
            }

            if (obj.parent_slot_values != null) {
                for (MappedObject parent : obj.parent_slot_values) {
                    if (! parent.visited) {
                        parents.add(parent);
                    }
                }
            }
        }

        for (MappedObject obj : visited_objects) {
            obj.visited = false;
        }

        return result;
    }

    public MappedObject slotLookup(String slot_name) throws KeyException {
        if (this.slot_values != null) {
            int slot_index = map.getSlot(slot_name);

            if (slot_index != -1) {
                return this.slot_values.get(slot_index);
            }
        }

        if (this.scope_parent != null) {
            MappedObject obj = this.scope_parent.getLocalSlot(slot_name);

            if (obj != null) {
                return obj;
            }
        }

        return parentLookup(slot_name);
    }
}
