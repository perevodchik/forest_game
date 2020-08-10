package com.perevodchik.forest.items;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemWeapon;
import com.perevodchik.forest.stats.AttributeValue;

public class ItemIronKnife extends ItemWeapon {
    public ItemIronKnife(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemIronKnife(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }

    @Override
    public void setAttributes() {
        addAttribute(Attribute.ATTACK, new AttributeValue(5f));
        addAttribute(Attribute.CRITICAL_CHANCE, new AttributeValue(5f));
        addAttribute(Attribute.CRITICAL_RATE, new AttributeValue(100f));
        addAttribute(Attribute.ATTACK_SPEED, new AttributeValue(0.6f));
    }
}
