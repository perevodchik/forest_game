package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.enums.AnimateState;
import com.perevodchik.forest.items.root.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EntityItem extends Entity {
    private final ItemStack stack;
    private boolean canTake = false;

    public EntityItem(ItemStack stack, World world, float width, float height, BodyDef.BodyType type) {
        super(world, width, height, type, stack.item().getTexture());
        this.stack = stack;
    }

    public boolean isCanTake() {
        return canTake;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setCanTake(boolean canTake) {
        this.canTake = canTake;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(stack.item() == null) {
            return;
        }
        getSprite().draw(batch, getBody());
    }

    @Override
    public void generateTextures() {
        HashMap<AnimateState, TextureRegion[]> animates = new HashMap<AnimateState, TextureRegion[]>() {{
            put(AnimateState.STAY, new TextureRegion[1]);
        }};
        if (getTexture() != null) {
            TextureRegion[][] tmp = TextureRegion.split(getTexture(), getTexture().getWidth(), getTexture().getHeight());
            int i = 0;
            for (Map.Entry<AnimateState, TextureRegion[]> e : animates.entrySet()) {
                System.arraycopy(tmp[i], 0, e.getValue(), 0, 1);
                i++;
            }
            animation = new Animation<>(1f, animates.get(AnimateState.STAY));
        }
    }

}
