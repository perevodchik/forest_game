package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.stats.DamageData;

public abstract class EntityBullet extends Entity {
    private DamageData data;
    private Entity owner;

    public EntityBullet(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
        getBody().setBullet(true);
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setData(DamageData data) {
        this.data = data;
    }

    public DamageData getData() {
        return data;
    }
}
