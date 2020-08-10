package com.perevodchik.forest.stats.inventory;

import com.badlogic.gdx.utils.StringBuilder;
import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;

import java.util.HashMap;
import java.util.Map;

public class EquipmentInventory implements IEquipmentInventory {
    private Map<Slot, ItemStack> equipItems;
    private Entity owner;

    public EquipmentInventory(Entity owner) {
        this.owner = owner;
        equipItems = new HashMap<>();
        equipItems.put(Slot.HEAD, ItemStack.empty());
        equipItems.put(Slot.BODY, ItemStack.empty());
        equipItems.put(Slot.MAIN_HAND, ItemStack.empty());
        equipItems.put(Slot.SECOND_HAND, ItemStack.empty());
        equipItems.put(Slot.LEFT_GLOVE, ItemStack.empty());
        equipItems.put(Slot.RIGHT_GLOVE, ItemStack.empty());
        equipItems.put(Slot.SHOE, ItemStack.empty());
        equipItems.put(Slot.TROUSERS, ItemStack.empty());
    }

    @Override
    public ItemStack reEquip(ItemStack stack) {
        ItemStack oldEquipItem = equipItems.get(stack.item().getSlot());
        equipItems.put(stack.item().getSlot(), stack);
        return oldEquipItem;
    }

    @Override
    public boolean canEquip(ItemStack stack) {
        return (!equipItems.containsKey(stack.item().getSlot()) || equipItems.get(stack.item().getSlot()) == null) && stack.item().isCanEquip() && checkStats(stack);
    }

    private boolean checkStats(ItemStack stack) {
        return true;
    }

    @Override
    public void equip(ItemStack stack) {
        if (equipItems.get(stack.item().getSlot()) != null) {
            equipItems.get(stack.item().getSlot()).set(stack);
            equipItems.get(stack.item().getSlot()).setEquip(true);
        }
    }

    @Override
    public void equip(Slot slot, ItemStack stack) {
        if(equipItems.containsKey(slot)) {
            equipItems.get(slot).set(stack);
            equipItems.get(slot).setEquip(true);
        }
    }

    @Override
    public void unEquip(ItemStack stack) {
        equipItems.put(stack.item().getSlot(), null);
        stack.setEquip(false);
    }

    @Override
    public void unEquip(Slot slot) {
        equipItems.put(slot, ItemStack.empty());
    }

    @Override
    public int getFreeSLots() {
        int freeSlots = 0;
        for(Map.Entry<Slot, ItemStack> e: equipItems.entrySet()) {
            if(e.getValue() == null)
                freeSlots++;
        }
        return freeSlots;
    }

    @Override
    public Map<Slot, ItemStack> getInventory() {
        return equipItems;
    }

    @Override
    public ItemStack get(Slot slot) {
        return equipItems.get(slot);
    }

    @Override
    public float getWeight() {
        float weight = 0f;
        for(ItemStack stack : equipItems.values()) {
            if(stack.item() == RegistryManager.empty) continue;
            weight += stack.item().getWeight() * stack.getCount();
        }
        return weight;
    }

    @Override
    public String toString() {
        StringBuilder _equipItems = new StringBuilder();
        for(Map.Entry<Slot, ItemStack> e: equipItems.entrySet()) {
            _equipItems.append("[").append(e.getKey().toString()).append("]").append("[").append(e.getValue().toString()).append("]").append("\n");
        }
        return "EquipmentInventory{" +
                "equipItems=" + _equipItems +
                '}';
    }
}
