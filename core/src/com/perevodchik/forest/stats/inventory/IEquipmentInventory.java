package com.perevodchik.forest.stats.inventory;

import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemStack;

import java.util.Map;

public interface IEquipmentInventory {
    ItemStack reEquip(ItemStack stack);
    boolean canEquip(ItemStack stack);
    void equip(ItemStack stack);
    void equip(Slot slot, ItemStack stack);
    void unEquip(ItemStack stack);
    void unEquip(Slot slot);
    int getFreeSLots();
    Map<Slot, ItemStack> getInventory();

    ItemStack get(Slot slot);
    float getWeight();
}