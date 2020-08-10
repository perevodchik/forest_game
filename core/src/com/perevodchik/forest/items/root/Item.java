package com.perevodchik.forest.items.root;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.stats.AttributeValue;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Item {
    private int maxStack = 1;
    private int id;
    private int maxDurability;
    private int durability;
    private int requireLvl;
    private float weight;
    private String registryName;
    private String displayName;
    private Slot slot;
    private Texture texture;
    private Map<Attribute, List<AttributeValue>> attributes = new HashMap<>();
    private boolean canRepair = true;
    private boolean canStack = false;
    private boolean canUpgrade = false;
    private boolean canDrop = true;
    private boolean canEquip = false;

    public Item(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        this.maxStack = maxStack;
        this.id = id;
        this.maxDurability = maxDurability;
        this.requireLvl = requireLvl;
        this.weight = weight;
        this.registryName = registryName;
        this.displayName = displayName;
        this.slot = slot;
        this.texture = texture;
        this.durability = this.maxDurability;
        setAttributes();
    }

    public Item(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        this.maxStack = maxStack;
        this.id = id;
        this.maxDurability = maxDurability;
        this.requireLvl = requireLvl;
        this.weight = weight;
        this.registryName = registryName;
        this.displayName = displayName;
        this.slot = slot;
        this.texture = texture;
        this.canEquip = canEquip;
        this.canStack = canStack;
        this.canUpgrade = canUpgrade;
        this.canDrop = canDrop;
        this.durability = this.maxDurability;
        setAttributes();
    }

    public void setAttributes() {}

    protected void addAttribute(Attribute attribute, AttributeValue attributeValue) {
        if (attributes.containsKey(attribute)) {
            attributes.get(attribute).add(attributeValue);
        } else  {
            List<AttributeValue> attributeValues = new ArrayList<>();
            attributeValues.add(attributeValue);
            attributes.put(attribute, attributeValues);
        }
    }

    boolean upgrade(Attribute attribute, AttributeValue attributeValue) {
        if(!canUpgrade || !canRepair) return false;
        if(attributes.containsKey(attribute)) {
            attributes.get(attribute).add(attributeValue);
            return true;
        }
        List<AttributeValue> attributeValues = new ArrayList<>();
        attributeValues.add(attributeValue);
        attributes.put(attribute, attributeValues);
        return true;
    }

    void takeDamage(int value) {
        durability -= value;
        if(durability < 0) {
            durability = 0;
            canRepair = false;
        }
    }

    boolean repair(int value) {
        if(!canRepair) return false;
        durability += value;
        if(durability > maxDurability) durability = maxDurability;
        return true;
    }

    public float getWeight() {
        return weight;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getId() {
        return id;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public int getDurability() {
        return durability;
    }

    public int getRequireLvl() {
        return requireLvl;
    }

    public String getRegistryName() {
        return registryName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Slot getSlot() {
        return slot;
    }

    public Texture getTexture() {
        return texture;
    }

    public Map<Attribute, List<AttributeValue>> getAttributes() {
        return attributes;
    }

    public boolean isCanRepair() {
        return canRepair;
    }

    public boolean isCanStack() {
        return canStack;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public boolean isCanDrop() {
        return canDrop;
    }

    public boolean isCanEquip() {
        return canEquip;
    }

    @Override
    public String toString() {
        return "Item{" + "maxStack=" + maxStack + ", id=" + id + ", requireLvl=" + requireLvl + ", weight=" + weight +
                ", registryName=" + registryName + ", displayName=" + displayName + ", slot=" + slot + ", canRepair=" + canRepair +
                ", canStack=" + canStack + ", canUpgrade=" + canUpgrade + ", canDrop=" + canDrop + ", canEquip=" + canEquip + '}';
    }
}