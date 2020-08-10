package com.perevodchik.forest.items;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemWeapon;
import com.perevodchik.forest.stats.AttributeValue;

public class ItemMagickList extends ItemWeapon {
    public ItemMagickList(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemMagickList(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }

    @Override
    public void setAttributes() {
        addAttribute(Attribute.ATTACK, new AttributeValue(2f));
        addAttribute(Attribute.CRITICAL_CHANCE, new AttributeValue(15f));
        addAttribute(Attribute.CRITICAL_RATE, new AttributeValue(50f));
        addAttribute(Attribute.ATTACK_SPEED, new AttributeValue(0.5f));
    }
}
