package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class EntityMob extends EntityLive {

    public EntityMob(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
    }

}
