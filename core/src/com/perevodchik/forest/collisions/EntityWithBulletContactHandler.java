package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityBullet;
import com.perevodchik.forest.entity.EntityLive;
import com.perevodchik.forest.entity.EntityStranger;
import com.perevodchik.forest.entity.Player;

public class EntityWithBulletContactHandler implements ContactHandler {
    private EntityLive live;
    private EntityBullet bullet;

    @Override
    public boolean isValid(Entity entity0, Entity entity1) {
        if(entity0 instanceof EntityLive && entity1 instanceof EntityBullet) {
            live = (EntityLive) entity0;
            bullet = (EntityBullet) entity1;
            return true;
        }
        if(entity1 instanceof EntityLive && entity0 instanceof EntityBullet) {
            live = (EntityLive) entity1;
            bullet = (EntityBullet) entity0;
            return true;
        }
        return false;
    }

    @Override
    public void collision() {
        if(!bullet.getData().getEntity().getId().equals(live.getId())) {
            float currentHealth = live.takeDamage(bullet.getData());
            bullet.getLocation().removeEntity(bullet.getId());
            if(bullet.getData().getEntity() instanceof Player) {
                Player player = (Player) bullet.getData().getEntity();
                if(currentHealth > 0)
                    player.setTarget(live);
                else
                    player.setTarget(null);
            }
        }
    }

    @Override
    public void endCollision() {
        live.getContacts().remove(this);
    }
}
