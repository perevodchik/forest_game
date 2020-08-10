package com.perevodchik.forest.items;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemBow;
import com.perevodchik.forest.stats.AttributeValue;

public class ItemWoodBow extends ItemBow {
    public ItemWoodBow(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemWoodBow(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }

    @Override
    public void setAttributes() {
        addAttribute(Attribute.ATTACK, new AttributeValue(7f));
        addAttribute(Attribute.CRITICAL_CHANCE, new AttributeValue(5f));
        addAttribute(Attribute.CRITICAL_RATE, new AttributeValue(80f));
        addAttribute(Attribute.ATTACK_SPEED, new AttributeValue(0.9f));
        addAttribute(Attribute.BULLET_SPEED, new AttributeValue(20f));
        addAttribute(Attribute.ENERGY_COST, new AttributeValue(2f));
    }
}
