package com.perevodchik.forest.items.root;

import com.perevodchik.forest.enums.ItemAction;
import com.perevodchik.forest.enums.Rarity;
import com.perevodchik.forest.registry.RegistryManager;

import java.util.UUID;

public class ItemStack {
    private UUID id = UUID.randomUUID();
    private int count;
    private int currentDurability;
    private boolean isEquip = false;
    private Rarity rarity = Rarity.COMMON;

    private Item item;

    public ItemStack(Item item) {
        this.count = 1;
        this.item = item;
    }

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = Math.min(count, item == null ? 0 : item.getMaxStack());
    }

    public ItemStack(Item item, int count, UUID uuid) {
        this.item = item;
        this.count = Math.min(count, item == null ? 0 : item.getMaxStack());
        this.id = uuid;
    }

    public static ItemStack empty() {
        return new ItemStack(RegistryManager.getInstance().getItemById(0), 0);
    }

    public static ItemStack from(ItemStack stack) {
        if(stack == null)
            return ItemStack.empty();
        return ItemStack.empty().set(stack);
    }

    public UUID getId() {
        return id;
    }

    public Item item() {
        return item;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setEmpty() {
        count = 0;
        item = RegistryManager.empty;
    }

    public ItemStack set(ItemStack stack) {
        this.item = stack.item;
        this.count = stack.getCount();
        this.id = stack.getId();
        this.isEquip = stack.isEquip();
        this.rarity = stack.getRarity();
        return this;
    }

    public ItemStack set(ItemStack stack, boolean cloneId) {
        this.item = stack.item;
        this.count = stack.getCount();
        if(cloneId)
            this.id = stack.getId();
        this.isEquip = stack.isEquip();
        this.rarity = stack.getRarity();
        return this;
    }

    public boolean isEquip() {
        return isEquip;
    }

    public ItemStack setEquip(boolean equip) {
        isEquip = equip;
        return this;
    }

    public ItemAction damage(int value) {
        currentDurability -= value;
        return currentDurability < 0 ? ItemAction.DESTROYED : ItemAction.NONE;
    }

    public void repair(int value) {
        currentDurability += value;
        if(currentDurability > item.getMaxDurability()) currentDurability = item.getMaxDurability();
    }

    public int getCurrentDurability() {
        return currentDurability;
    }

    public ItemStack setCount(int count) {
        this.count = count;
        return this;
    }

    public int getCount() {
        return count;
    }

    public int canAdd() {
        return item.isCanStack() ? item.getMaxStack() - count : 0;
    }

    public void add(int val) {
        count += val;
    }

    public void remove(int val) {
        count -= val;
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "id=" + id +
                ", count=" + count +
                ", rarity=" + rarity +
                ", item=" + item +
                '}';
    }
}
