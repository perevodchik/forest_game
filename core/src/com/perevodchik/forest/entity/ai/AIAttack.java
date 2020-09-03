package com.perevodchik.forest.entity.ai;

import com.perevodchik.forest.entity.EntityMob;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.utils.Vector;

public class AIAttack implements AI {
    private EntityMob mob;
    private long tick = 0;

    public AIAttack(EntityMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean execute() {
        if(canContinue()) {
            float r = mob.rotateTo(Player.getPlayer().getBody().getPosition());
            mob.setAngle(r);
            mob.attack();
        } else {
            mob.setAi(getNewAI());
        }
        return false;
    }

    @Override
    public boolean update() {
        tick++;
        if(tick % 20 == 0)
            if(!mob.isAttack() && !mob.isDead())
                return execute();
        return false;
    }

    @Override
    public int getCurrentSteep() {
        return 0;
    }

    @Override
    public boolean canContinue() {
        double distance = Vector.distance(Player.getPlayer().getBody().getPosition(), mob.getBody().getPosition());
        return distance <= mob.getAttackRange();
    }

    @Override
    public AI getNewAI() {
        if(Vector.distance(Player.getPlayer().getBody().getPosition(), mob.getBody().getPosition()) >= mob.getAttackRange())
            return new AIFollow(mob);
        else return this;
    }
}
