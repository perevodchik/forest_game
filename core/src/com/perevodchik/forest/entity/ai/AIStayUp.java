package com.perevodchik.forest.entity.ai;

import com.perevodchik.forest.entity.EntityMob;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.utils.Vector;

public class AIStayUp implements AI {
    private EntityMob mob;
    private long tick = 0;

    public AIStayUp(EntityMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean update() {
        tick++;
        if(tick % 20 == 0) {
            AI ai = getNewAI();
            if(ai != null) {
                mob.setAi(ai);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCurrentSteep() {
        return 0;
    }

    @Override
    public boolean canContinue() {
        return !mob.isDead();
    }

    @Override
    public AI getNewAI() {
        if(Vector.distance(Player.getPlayer().getBody().getPosition(), mob.getBody().getPosition()) < 10)
            return new AIFollow(mob);
        return this;
    }
}
