package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class EntityArrow extends EntityBullet {
    private Vector2 velocity;

    public EntityArrow(World world, float width, float height, BodyDef.BodyType type, Texture texture, Vector2 position, Vector2 velocity, float angle) {
        super(world, width, height, type, texture);
        this.velocity = velocity;
        this.setAngle(angle);
        this.generateTextures();
        getBody().setTransform(position, angle);
        System.out.println(velocity.toString());
    }

    @Override
    public void setTarget(Entity target) {
        Vector2 direction = new Vector2(Player.getPlayer().getBody().getPosition().x, Player.getPlayer().getBody().getPosition().y);
        direction.sub(getBody().getPosition());
        direction.nor();

        float speed = getSpeed();
        direction.scl(speed);
    }

    @Override
    public void generateTextures() {
        getSprite().setRegion(getTexture());
    }

    @Override
    public void render(SpriteBatch batch) {
        getSprite().draw(batch, getBody());
    }

    @Override
    public void update(float dt) {
        tick++;

//        {
//
//            getBody().setLinearVelocity(velocity);
//        }

        getBody().setLinearVelocity(velocity.x, velocity.y);
    }
}
