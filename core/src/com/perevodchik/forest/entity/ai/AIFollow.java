package com.perevodchik.forest.entity.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.perevodchik.forest.entity.EntityMob;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.utils.Vector;

public class AIFollow implements AI {
    private EntityMob mob;
    private long tick = 0;

    public AIFollow(EntityMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean execute() {
        Vector2 direction = new Vector2(Player.getPlayer().getBody().getPosition().x, Player.getPlayer().getBody().getPosition().y);
        direction.sub(mob.getBody().getPosition());
        direction.nor();

        float speed = mob.getSpeed();
        mob.setVelocity(direction.scl(speed));
        return true;
    }

    @Override
    public boolean update() {
        tick++;
        boolean result = execute();
        if(tick % 10 == 0) {
            mob.setAi(getNewAI());
        }
        return result;
    }

    @Override
    public int getCurrentSteep() {
        return 0;
    }

    @Override
    public boolean canContinue() {
        return Vector.distance(Player.getPlayer().getBody().getPosition(), mob.getBody().getPosition()) < 10;
    }

    @Override
    public AI getNewAI() {
        double distance = Vector.distance(Player.getPlayer().getBody().getPosition(), mob.getBody().getPosition());
        if(distance >= 10)
            return new AIStayUp(mob);
        else if(distance <= mob.getAttackRange() && !mob.isAttack())
            return new AIAttack(mob);
        return this;
    }
}
