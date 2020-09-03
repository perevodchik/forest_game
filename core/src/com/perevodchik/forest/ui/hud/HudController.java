package com.perevodchik.forest.ui.hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.blacksmith.BlacksmithWindow;
import com.perevodchik.forest.ui.inventory.InventoryWindow;

public class HudController extends InputListener {
    private final Hud hud;

    public HudController(Hud hud) {
        this.hud = hud;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        GameStateManager.getGameStateManager().peek().showWindow(
                new InventoryWindow(
                        new Padding(),
                        0,
                        0,
                        ForestGameScreen.width,
                        ForestGameScreen.height)
        );

        ForestGameScreen.isPause = true;
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
    }
}