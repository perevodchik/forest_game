package com.perevodchik.forest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.perevodchik.forest.collisions.ContactHandler;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.stats.DamageData;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.enums.AnimateState;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.enums.DamageSource;
import com.perevodchik.forest.utils.DropItemData;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.locations.Location;
import com.perevodchik.forest.stats.AttributeValue;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public abstract class Entity extends Actor {
    private final Map<Attribute, List<AttributeValue>> attributeMap;
    private final List<DropItemData> dropList = new ArrayList<>();
    private final Set<ContactHandler> contacts = new HashSet<>();
    private final Body body;
    private final World world;
    private Location location;
    private Vector2 velocity = new Vector2(0, 0);
    private Entity target;
    protected long tick;

    private static final float maxSpeed = 20;
    private final float radius;
    private float angle = 0;
    private float maxHealth = 20;
    private float currentHealth = 1;
    private float maxEnergy = 20;
    private float currentEnergy = 1;
    private float healthRegen = 0.5f;
    private float energyRegen = 0.5f;
    private float speed = 3f;
    private final UUID id = UUID.randomUUID();
    private boolean isMoving = false;
    private boolean isAttack = false;
    private boolean isDead = false;
    private boolean canTakeDamage = true;
    private Timer timer;

    private Texture texture;
    private final Box2DSprite sprite;
    protected AnimateState animateState;
    protected Animation<TextureRegion> animation;
    private HashMap<AnimateState, TextureRegion[]> animates;
    private float stateTime;

    public Entity(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        attributeMap = new HashMap<>();
        this.world = world;
        setWidth(width * ForestGameScreen.UNIT_SCALE);
        setHeight(height * ForestGameScreen.UNIT_SCALE);
        radius = width * ForestGameScreen.UNIT_SCALE;
        {
            BodyDef def = new BodyDef();
            def.type = type;
            this.body = world.createBody(def);

            CircleShape poly = new CircleShape();
            poly.setRadius(radius);
            body.createFixture(poly, 100);
            body.setFixedRotation(true);
            poly.dispose();
        }

        body.setLinearDamping(10);
        body.setUserData(this);
        body.setFixedRotation(true);

        this.sprite = new Box2DSprite();
        this.sprite.setWidth(width);
        this.sprite.setHeight(height);
        this.texture = texture;
        this.generateTextures();
        initAttribute();
        recalculateStats();
        initDropList();
    }

    public Entity(World world, Body body) {
        attributeMap = new HashMap<>();
        this.world = world;
        setWidth(0);
        setHeight(0);
        radius = 0;

        this.body = body;
        body.setLinearDamping(10);
        body.setUserData(this);
        body.setFixedRotation(true);

        this.sprite = null;
        initAttribute();
        initDropList();
    }

    public void dropItems() {
        float x, y;
        final Random r = new Random();
        for(DropItemData data: dropList) {
            int roll = r.nextInt(101);
            if (roll >= data.chanceData.min && roll <= data.chanceData.max) {
                int count = data.countData.min == 1 && data.countData.max == 1 ? 1 :
                        r.nextInt(data.countData.max - data.countData.min) + data.countData.min;
                ItemStack stack = ItemStack
                        .empty()
                        .set(data.stack, false)
                        .setCount(count);
                EntityItem item = new EntityItem(stack, world, 3, 3, BodyDef.BodyType.DynamicBody);
                item.setLocation(getLocation());
                x = r.nextBoolean() ? getBody().getPosition().x + (r.nextFloat() / 3) : getBody().getPosition().x - (r.nextFloat() / 3);
                y = r.nextBoolean() ? getBody().getPosition().y + (r.nextFloat() / 3) : getBody().getPosition().y - (r.nextFloat() / 3);
                item.getBody().setTransform(x, y, 0);
                location.getActorsToBeAdded().add(item);
            }
        }
    }

    public void initDropList() {}

    public void initAttribute() {
        ArrayList<AttributeValue> healthAttributeList = new ArrayList<>();
        healthAttributeList.add(new AttributeValue(4f));
        ArrayList<AttributeValue> attackAttributeList = new ArrayList<>();
        attackAttributeList.add(new AttributeValue(2f));
        ArrayList<AttributeValue> healthRegenAttributeList = new ArrayList<>();
        healthRegenAttributeList.add(new AttributeValue(1f));
        ArrayList<AttributeValue> energyRegenAttributeList = new ArrayList<>();
        energyRegenAttributeList.add(new AttributeValue(1f));

        attributeMap.put(Attribute.HEALTH, healthAttributeList);
        attributeMap.put(Attribute.ATTACK, attackAttributeList);
        attributeMap.put(Attribute.HEALTH_REGEN, healthRegenAttributeList);
        attributeMap.put(Attribute.ENERGY_REGEN, energyRegenAttributeList);
    }

    public void attack() {
        float cost = attribute(Attribute.ENERGY_COST);
        if(isMoving) cost *= 1.7;
        if(getCurrentEnergy() < cost) return;
        decreaseCurrentEnergy(cost);

        float attackSpeed = 1f;
        float bulletSpeed = maxSpeed / 3 + getSpeed();
        if(this instanceof Player) {
            attackSpeed = attribute(Attribute.ATTACK_SPEED);
            Map<Attribute, List<AttributeValue>> attributes = ((Player) get()).getEquipmentInventory().get(Slot.MAIN_HAND).item().getAttributes();
            if(attributes.containsKey(Attribute.BULLET_SPEED)) {
                if(!attributes.get(Attribute.BULLET_SPEED).isEmpty()) {
                    bulletSpeed = 0f;
                    for (AttributeValue value: attributes.get(Attribute.BULLET_SPEED)) {
                        bulletSpeed += value.value;
                    }
                }
            }
        }
        float finalBulletSpeed = bulletSpeed;
        setAttack(true);
        updateAnimation(AnimateState.ATTACK);
        if(timer != null)
            timer.clear();
        timer = new Timer();
        Timer.Task task0 = new Timer.Task() {
            @Override
            public void run() {
                float angle = body.getAngle();
                Vector2 direction = new Vector2(MathUtils.cos(angle) * finalBulletSpeed, MathUtils.sin(angle) * finalBulletSpeed);
                Vector2 v2 = new Vector2(
                        (float) (getBody().getPosition().x + (radius * 1.5) * Math.cos(angle)),
                        (float) (getBody().getPosition().y + (radius * 1.5) * Math.sin(angle)));
                EntityBullet arrow = new EntityArrow(
                        getBody().getWorld(),
                        5,
                        5,
                        BodyDef.BodyType.DynamicBody,
                        new Texture("arrow0.png"),
                        v2.cpy(),
                        direction.cpy(),
                        angle);
                arrow.setOwner(get());
                if(target != null)
                    arrow.setTarget(target);
                DamageData data = new DamageData(get(),
                        attribute(Attribute.ATTACK),
                        DamageSource.ENTITY);
                arrow.setData(data);
                arrow.setLocation(location);
                getLocation().spawnEntity(arrow);
            }
        };
        Timer.Task task1 = new Timer.Task() {
            @Override
            public void run() {
                setAttack(false);
                updateAnimation(AnimateState.STAY);
            }
        };
        timer.scheduleTask(task0, attackSpeed * 0.3f);
        timer.scheduleTask(task1, attackSpeed * 0.5f);
    }

    public float attribute(Attribute attribute) {
        float attributeValue = 0;
        if (attributeMap.containsKey(attribute)) {
            List<AttributeValue> values = attributeMap.get(attribute);
            for (AttributeValue value : values)
                attributeValue += value.value;
        }
        return attributeValue;
    }

    public Map<Attribute, List<AttributeValue>> attributeMap() {
        return attributeMap;
    }

    public void recalculateStats() {
        if (attributeMap.containsKey(Attribute.SPEED)) {
            float speed = 0;
            for (AttributeValue value : attributeMap.get(Attribute.SPEED)) {
                speed += value.value;
            }
            Gdx.app.log(String.valueOf(Attribute.SPEED), speed + "");
            setSpeed(speed);
        }
        if (attributeMap.containsKey(Attribute.HEALTH)) {
            float health = 0;
            for (AttributeValue value : attributeMap.get(Attribute.HEALTH)) {
                health += value.value;
            }
            Gdx.app.log(String.valueOf(Attribute.HEALTH), health + "");
            setHealth(health);
        }
        if (attributeMap.containsKey(Attribute.ENERGY)) {
            float energy = 0;
            for (AttributeValue value : attributeMap.get(Attribute.ENERGY)) {
                energy += value.value;
            }
            Gdx.app.log(String.valueOf(Attribute.ENERGY), energy + "");
            setCurrentEnergy(energy);
        }
        if (attributeMap.containsKey(Attribute.HEALTH_REGEN)) {
            float healthRegen = 0;
            for (AttributeValue value : attributeMap.get(Attribute.HEALTH_REGEN)) {
                healthRegen += value.value;
            }
            Gdx.app.log(String.valueOf(Attribute.HEALTH_REGEN), healthRegen + "");
            this.healthRegen = healthRegen;
        }
        if (attributeMap.containsKey(Attribute.ENERGY_REGEN)) {
            float energyRegen = 0;
            for (AttributeValue value : attributeMap.get(Attribute.ENERGY_REGEN)) {
                energyRegen += value.value;
            }
            Gdx.app.log(String.valueOf(Attribute.ENERGY_REGEN), energyRegen + "");
            this.energyRegen = energyRegen;
        }
    }

    public void generateTextures() {
        HashMap<AnimateState, TextureRegion[]> animates = new HashMap<AnimateState, TextureRegion[]>() {{
            put(AnimateState.STAY, new TextureRegion[4]);
            put(AnimateState.WALK, new TextureRegion[4]);
            put(AnimateState.ATTACK, new TextureRegion[4]);
            put(AnimateState.SPELL, new TextureRegion[4]);
        }};
        this.animates = animates;

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight() / 4);
        int i = 0;
        for (Map.Entry<AnimateState, TextureRegion[]> e : animates.entrySet()) {
            System.arraycopy(tmp[i], 0, e.getValue(), 0, 4);
            i++;
        }
        updateAnimationImmediately(AnimateState.STAY);
    }

    public void setDead() {
        isDead = true;
        getLocation().removeEntity(getId());
    }

    public void decreaseCurrentEnergy(float value) {
        currentEnergy -= value;
    }

    public void decreaseCurrentHealth(float value) {
        if(currentHealth < value)
            currentHealth = 0;
        else
            currentHealth -= value;
    }

    public void setVelocity(Vector2 v) {
        setVelocity(v.x, v.y);
    }

    public void setVelocity(float x, float y) {
        float velocityX = x;
        float velocityY = y;
        if(isAttack()) {
            velocityX *= 0.3f;
            velocityY *= 0.3f;
        }
        if (x == 0 && y == 0) {
            isMoving = false;
            if(animateState != AnimateState.STAY)
                updateAnimation(AnimateState.STAY);
        }
        else {
            if(target == null)
                angle = getAngle(body.getLinearVelocity());
            else
                angle = rotateTo(target.getBody().getPosition());
            isMoving = true;
            velocity.set(velocityX, velocityY);
            if(animateState != AnimateState.ATTACK)
                updateAnimation(AnimateState.WALK);
        }
        body.setLinearVelocity(velocityX * speed, velocityY * speed);
        body.setTransform(getBody().getPosition(), angle);
    }

    public static float getAngle(Vector2 vector) {
        return ((float) Math.atan2(vector.y, vector.x));
    }

    public float rotateTo(Vector2 to) {
        return new Vector2(to.x, to.y).sub(body.getPosition()).angleRad();
    }

    public void updateAnimation(AnimateState animateState) {
        if (this.animateState != animateState) {
            this.animateState = animateState;
            animation = new Animation<>((float) 1 / 4, animates.get(animateState));
        }
    }

    public void updateAnimationImmediately(AnimateState animateState) {
        this.animateState = animateState;
        animation = new Animation<>((float) 1 / 4, animates.get(animateState));
    }

    public void render(SpriteBatch batch) {
        if (sprite != null)
            sprite.draw(batch, body);
    }

    public void update(float dt) {
        tick++;
        stateTime += dt;

        if (sprite != null && animation != null)
            sprite.setRegion(animation.getKeyFrame(stateTime, true));
        if (tick % 100 == 0) {
            currentHealth += healthRegen;
            if (currentHealth > maxHealth) currentHealth = maxHealth;
            currentEnergy += energyRegen;
            if (currentEnergy > maxEnergy) currentEnergy = maxEnergy;
        }
        if(target != null) {
            float distance = target.getBody().getPosition().dst(getBody().getPosition());
            if(distance > 3) {
                setTarget(null);
            }
        }
        body.setTransform(body.getPosition(), angle);
    }

    /* GETTERS AND SETTERS */

    public void setCurrentEnergy(float currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public List<DropItemData> getDropList() {
        return dropList;
    }

    Texture getTexture() {
        return texture;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getCurrentEnergy() {
        return currentEnergy;
    }

    public float getMaxEnergy() {
        return maxEnergy;
    }

    public UUID getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Box2DSprite getSprite() {
        return sprite;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public static float getMaxSpeed() {
        return maxSpeed;
    }

    private void setHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        if(getCurrentHealth() > maxHealth)
            setCurrentHealth(maxHealth);
    }

    public Body getBody() {
        return body;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public World getWorld() {
        return world;
    }

    public Set<ContactHandler> getContacts() {
        return contacts;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isMoving() {
        return isMoving;
    }

    private Entity get() {
        return this;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void setCanTakeDamage(boolean canTakeDamage) {
        this.canTakeDamage = canTakeDamage;
    }

    public boolean isCanTakeDamage() {
        return canTakeDamage;
    }

    public double getAttackRange() {
        return 0.8;
    }
}