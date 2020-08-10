package com.perevodchik.forest.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.locations.DungeonLocation;
import com.perevodchik.forest.locations.Location;

public class NextStageButton extends Actor {
    private Texture texture;

    public NextStageButton() {
        int x = (int) (ForestGameScreen.width * 0.5), y = (int) (ForestGameScreen.height * 0.5);
        int width = ForestGameScreen.blockY, height = ForestGameScreen.blockY;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        addListener(new DestroyItemListener());
        texture = new Texture("shield.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(Player.canGoNextStage) {
            super.draw(batch, parentAlpha);
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

    private static class DestroyItemListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("hit");
            if(!Player.canGoNextStage)
                return true;
            Location location = GameStateManager.getGameStateManager().peek();
            if(location instanceof DungeonLocation) {
                System.out.println("location is dungeon");
                    ((DungeonLocation) location).nextStage();
                    Player.canGoNextStage = false;
            }
            return true;
        }
    }
}
