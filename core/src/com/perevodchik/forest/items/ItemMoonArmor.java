package com.perevodchik.forest.items;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.Item;
import com.perevodchik.forest.stats.AttributeValue;

public class ItemMoonArmor extends Item {
    public ItemMoonArmor(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemMoonArmor(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }

    @Override
    public void setAttributes() {
        addAttribute(Attribute.ARMOR, new AttributeValue(3f));
        addAttribute(Attribute.HEALTH, new AttributeValue(20f));
        addAttribute(Attribute.HEALTH_REGEN, new AttributeValue(2.5f));
    }
}
