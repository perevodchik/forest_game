package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityItem;
import com.perevodchik.forest.entity.Player;

public class EntityWithItemContactHandler implements ContactHandler {
    Player entity;
    EntityItem item;
    @Override
    public boolean isValid(Entity entity0, Entity entity1) {
        if(entity0 instanceof Player && entity1 instanceof EntityItem) {
            entity = (Player) entity0;
            item = (EntityItem) entity1;
            return true;
        }
        if(entity1 instanceof Player && entity0 instanceof EntityItem) {
            entity = (Player) entity1;
            item = (EntityItem) entity0;
            return true;
        }
        return false;
    }

    @Override
    public void collision() {
        if (item.isCanTake()) {
            entity.pickItem(item.getStack());
            if(item.getLocation() != null)
                item.getLocation().removeEntity(item.getId());
        }
    }

    @Override
    public void endCollision() {

    }
}
