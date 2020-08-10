package com.perevodchik.forest.items.root;

import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.enums.Slot;

public abstract class ItemBow extends Item {

    public ItemBow(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, com.perevodchik.forest.enums.Slot slot, Texture texture) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture);
    }

    public ItemBow(int maxStack, int id, int maxDurability, int requireLvl, float weight, String registryName, String displayName, Slot slot, Texture texture, boolean canEquip, boolean canStack, boolean canUpgrade, boolean canDrop) {
        super(maxStack, id, maxDurability, requireLvl, weight, registryName, displayName, slot, texture, canEquip, canStack, canUpgrade, canDrop);
    }
}
