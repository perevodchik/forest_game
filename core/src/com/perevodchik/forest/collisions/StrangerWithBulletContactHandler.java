package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityBullet;
import com.perevodchik.forest.entity.EntityStranger;

public class StrangerWithBulletContactHandler implements ContactHandler {
    private EntityStranger stranger;
    private EntityBullet bullet;

    @Override
    public boolean isValid(Entity entity0, Entity entity1) {
        if(entity0 instanceof EntityStranger && entity1 instanceof EntityBullet) {
            stranger = (EntityStranger) entity0;
            bullet = (EntityBullet) entity1;
            return true;
        }
        if(entity1 instanceof EntityStranger && entity0 instanceof EntityBullet) {
            stranger = (EntityStranger) entity1;
            bullet = (EntityBullet) entity0;
            return true;
        }
        return false;
    }

    @Override
    public void collision() {
        bullet.getLocation().removeEntity(bullet.getId());
    }

    @Override
    public void endCollision() { }
}
