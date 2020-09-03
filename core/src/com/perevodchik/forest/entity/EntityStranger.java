package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.enums.Strangers;
import com.perevodchik.forest.utils.Vector;

public class EntityStranger extends EntityLive {
    private Strangers type;
    private double distanceInteract = 1.5;

    public EntityStranger(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
        setCanTakeDamage(false);
    }

    public EntityStranger(World world, float width, float height, BodyDef.BodyType type, Texture texture, double distanceInteract) {
        super(world, width, height, type, texture);
        this.distanceInteract = distanceInteract;
        setCanTakeDamage(false);
    }

    public double getDistanceInteract() {
        return distanceInteract;
    }

    public boolean canInteract(Entity e) {
        if(!(e instanceof Player))
            return false;
        double distance =  Vector.distance(getBody().getPosition(), e.getBody().getPosition());
        return distance <= distanceInteract;
    }

    public Strangers getType() {
        return type;
    }

    public void setType(Strangers type) {
        this.type = type;
    }
}
