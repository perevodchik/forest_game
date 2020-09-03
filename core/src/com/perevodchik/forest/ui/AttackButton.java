package com.perevodchik.forest.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.locations.DungeonLocation;
import com.perevodchik.forest.locations.Location;
import com.perevodchik.forest.registry.RegistryManager;
import com.perevodchik.forest.ui.blacksmith.BlacksmithWindow;

public class AttackButton extends Actor {
    private Texture texture;
    private ItemStack stack;

    public AttackButton(Texture texture, Player player) {
        this.texture = texture;

        float width = ForestGameScreen.height * 0.2f;
        float height = ForestGameScreen.height * 0.2f;
        int x = (int) (ForestGameScreen.width * 0.85);
        int y = (int) ((int) (ForestGameScreen.height * 0.35) - height);

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        addListener(new AttackButtonController());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stack = Player.getPlayer().getEquipmentInventory().get(Slot.MAIN_HAND);
        if(stack.item() == RegistryManager.empty)
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        else
            batch.draw(stack.item().getTexture(), getX(), getY(), getWidth(), getHeight());
    }

    private class AttackButtonController extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            Player.getPlayer().attack();
        }
    }
}
