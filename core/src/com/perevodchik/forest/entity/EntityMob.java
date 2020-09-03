package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.entity.ai.AI;
import com.perevodchik.forest.entity.ai.AIFollow;
import com.perevodchik.forest.entity.ai.AIStayUp;

public class EntityMob extends EntityLive implements AIEntity{
    private AI ai = new AIStayUp(this);
    public EntityMob(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
        initAI();
    }

    @Override
    public void tickAI() {
        if(ai != null) {
            ai.update();
        }
    }

    @Override
    public void initAI() {
        aiList.add(new AIFollow(this));
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        try {
            tickAI();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
