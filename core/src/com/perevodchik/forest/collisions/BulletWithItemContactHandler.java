package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityBullet;
import com.perevodchik.forest.entity.EntityItem;

import java.util.Random;

public class BulletWithItemContactHandler implements ContactHandler {
    private static Random r = new Random();
    private EntityItem item;
    private EntityBullet bullet;

    @Override
    public boolean isValid(Entity entity0, Entity entity1) {
        if(entity0 instanceof EntityItem && entity1 instanceof EntityBullet) {
            item = (EntityItem) entity0;
            bullet = (EntityBullet) entity1;
            return true;
        }
        if(entity1 instanceof EntityItem && entity0 instanceof EntityBullet) {
            item = (EntityItem) entity1;
            bullet = (EntityBullet) entity0;
            return true;
        }
        return false;
    }

    @Override
    public void collision() {
        bullet.getLocation().removeEntity(bullet.getId());
        bullet.getLocation().removeEntity(bullet.getId());
        if(r.nextInt(5) == 2)
            item.getLocation().removeEntity(item.getId());
    }

    @Override
    public void endCollision() {

    }
}
