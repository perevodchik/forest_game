package com.perevodchik.forest.stats.inventory;

import com.perevodchik.forest.items.root.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IBasicInventory {
    void set(ItemStack stack, int slot);
    void addItem(ItemStack stack);
    void remove(int index);
    void remove(UUID id);
    void remove(ItemStack stack);
    int count(ItemStack item);
    void replace(ItemStack oldStack, ItemStack newStack);
    void dropInventory();
    void show();
    float getWeight();
    int getItemsCount();
    ItemStack get(int index);
    List<ItemStack> get();
}
