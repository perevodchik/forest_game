package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.enums.Strangers;
import com.perevodchik.forest.utils.Vector;

public class EntityStranger extends EntityLive {
    private Strangers type;
    private double distanceInteract = 0.4;

    public EntityStranger(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
    }

    public EntityStranger(World world, float width, float height, BodyDef.BodyType type, Texture texture, double distanceInteract) {
        super(world, width, height, type, texture);
        this.distanceInteract = distanceInteract;
    }

    public double getDistanceInteract() {
        return distanceInteract;
    }

    public boolean canInteract(Entity e) {
        if(!(e instanceof Player))
            return false;
        return Vector.distance(getBody().getPosition(), e.getBody().getPosition()) <= distanceInteract;
    }

    public Strangers getType() {
        return type;
    }
}
