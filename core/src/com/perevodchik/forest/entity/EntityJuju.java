package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.utils.MinMax;
import com.perevodchik.forest.utils.DropItemData;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;

public class EntityJuju extends EntityMob {

    public EntityJuju(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
    }

    @Override
    public void initDropList() {
        getDropList().add(new DropItemData(new ItemStack(RegistryManager.goldenCoin), new MinMax(10, 40), new MinMax(30, 100)));
        getDropList().add(new DropItemData(new ItemStack(RegistryManager.magicList), new MinMax(1, 1), new MinMax(40, 55)));
        getDropList().add(new DropItemData(new ItemStack(RegistryManager.woodBow), new MinMax(1, 1), new MinMax(1, 10)));
        getDropList().add(new DropItemData(new ItemStack(RegistryManager.ironArmor), new MinMax(1, 1), new MinMax(20, 35)));
    }
}
