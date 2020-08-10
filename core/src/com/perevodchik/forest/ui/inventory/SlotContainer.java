package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.ui.Slot;

public class SlotContainer extends Group {
    private static final Texture background = new Texture("stone.png");
    private boolean isDrawBackground;

    public SlotContainer(int x, int y, int width, int height, boolean isDrawBackground) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.isDrawBackground = isDrawBackground;
        if(ForestGameScreen.isDebug)
            debug();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isDrawBackground)
            batch.draw(background, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }
}
