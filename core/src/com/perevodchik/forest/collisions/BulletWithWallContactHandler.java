package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityBullet;
import com.perevodchik.forest.entity.EntityWall;

public class BulletWithWallContactHandler implements ContactHandler {
    EntityBullet bullet;
    EntityWall wall;
    @Override
    public boolean isValid(Entity entity0, Entity entity1) {
        if(entity0 instanceof EntityBullet && entity1 instanceof EntityWall) {
            bullet = (EntityBullet) entity0;
            wall = (EntityWall) entity1;
            return true;
        }
        if(entity1 instanceof EntityBullet && entity0 instanceof EntityWall) {
            bullet = (EntityBullet) entity1;
            wall = (EntityWall) entity0;
            return true;
        }
        return false;
    }

    @Override
    public void collision() {
        bullet.getLocation().removeEntity(bullet.getId());
    }

    @Override
    public void endCollision() {

    }
}
