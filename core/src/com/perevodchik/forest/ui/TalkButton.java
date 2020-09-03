package com.perevodchik.forest.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.EntityStranger;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.ui.blacksmith.BlacksmithWindow;
import com.perevodchik.forest.utils.Vector;

public class TalkButton extends Actor {
    private Texture texture;
    private EntityStranger stranger;

    public TalkButton(Texture imageUp, EntityStranger stranger) {
        this.texture = imageUp;
        this.stranger = stranger;

        float width = ForestGameScreen.height * 0.12f;
        float height = ForestGameScreen.height * 0.12f;
        int x = (int) (ForestGameScreen.width * 0.9);
        int y = (int) ((int) ForestGameScreen.height * 0.5);

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        addListener(new TalkButtonListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (stranger != null)
            if (stranger.canInteract(Player.getPlayer()))
                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    private class TalkButtonListener extends InputListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (stranger != null) {
                if (Vector.distance(Player.getPlayer().getBody().getPosition(), stranger.getBody().getPosition()) <= stranger.getDistanceInteract())
                    switch (stranger.getType()) {
                        case BLACKSMITH:
                            GameStateManager.getGameStateManager().peek().showWindow(
                                    new BlacksmithWindow(new Padding())
                            );
                            break;
                        case CLERIC:
                            break;
                    }
            }
        }
    }

}
