package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.stats.DamageData;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.enums.Attribute;

import java.util.Random;


public class EntityLive extends Entity {
    private Body attackBody;

    public long lastAttackTime = 0;
    private float attackAngle = 0;

    public EntityLive(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
    }

    public float takeDamage(DamageData damageData) {
        float armorReduce = attribute(Attribute.ARMOR) / 100;
        float criticalChance = damageData.getEntity().attribute(Attribute.CRITICAL_CHANCE);
        float criticalRate = damageData.getEntity().attribute(Attribute.CRITICAL_RATE);
        float damageValue = damageData.getValue();

        int rand = new Random().nextInt(100) + 1;
        if(criticalChance >= rand) {
            damageValue *= criticalRate / 100;
        }
        float d = damageValue - (damageValue * armorReduce);
        if(d < 0.5f)
            d = 0.5f;
        decreaseCurrentHealth(d);
        if(getCurrentHealth() <= 0) {
            setDead();
        }
        return getCurrentHealth();
    }

    public void createAttackBody(float width) {
        BodyDef attackDef = new BodyDef();
        attackDef.type = BodyDef.BodyType.DynamicBody;
        this.attackBody = getWorld().createBody(attackDef);

        CircleShape attackShape = new CircleShape();
        attackShape.setRadius((width * ForestGameScreen.UNIT_SCALE) * 2);
        attackBody.createFixture(attackShape, 1);
        attackShape.dispose();
        attackBody.getPosition().set(getBody().getPosition());
        attackBody.setActive(false);
        attackBody.setBullet(true);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
