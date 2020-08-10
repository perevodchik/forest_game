package com.perevodchik.forest.items;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.Item;
import com.perevodchik.forest.stats.AttributeValue;

public class ItemIronArmor extends Item {
    public ItemIronArmor(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemIronArmor(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }

    @Override
    public void setAttributes() {
        addAttribute(Attribute.ARMOR, new AttributeValue(10f));
        addAttribute(Attribute.HEALTH, new AttributeValue(2f));
        addAttribute(Attribute.STRENGTH, new AttributeValue(5f));
    }
}
