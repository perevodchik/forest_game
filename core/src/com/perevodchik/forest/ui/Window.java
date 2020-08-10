package com.perevodchik.forest.ui;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.perevodchik.forest.ForestGameScreen;

import java.util.UUID;

public class Window extends Group {
    private final UUID id = UUID.randomUUID();
    private float x, y;
    private float width, height;
    private Padding padding;
    private Texture background = new Texture("floor.png");

    public Window(Padding padding, float x, float y, int width, int height) {
        this.padding = padding;
        setX(x + padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setY(y + padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        setWidth(width - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setHeight(height - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));

        this.x = getX();
        this.y = getY();
        this.width = getWidth();
        this.height = getHeight();

        debugAll();
        initUI();
    }

    public Window(Padding padding, int width, int height) {
        this.padding = padding;
        setX(padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setY(padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        setWidth(width - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setHeight(height - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));

        this.x = getX();
        this.y = getY();
        this.width = getWidth();
        this.height = getHeight();

        debugAll();
        initUI();
    }

    public Window(Padding padding) {
        this.padding = padding;
        setX(padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setY(padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        setWidth(ForestGameScreen.width - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        setHeight(ForestGameScreen.height - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));

        this.x = getX();
        this.y = getY();
        this.width = getWidth();
        this.height = getHeight();

        debugAll();
        initUI();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(background != null)
            batch.draw(background, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    public void initUI() { }

    public void validatePadding(Actor actor) {
        actor.setX(actor.getX() + padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        actor.setY(actor.getY() + padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
        actor.setWidth(actor.getWidth() - padding.getPadding(com.perevodchik.forest.enums.Padding.RIGHT) - padding.getPadding(com.perevodchik.forest.enums.Padding.LEFT));
        actor.setHeight(actor.getHeight() - padding.getPadding(com.perevodchik.forest.enums.Padding.TOP) - padding.getPadding(com.perevodchik.forest.enums.Padding.BOTTOM));
    }

    public Texture getBackground() {
        return background;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public UUID getId() {
        return id;
    }

    public Padding getPadding() {
        return padding;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        this.y = y;
        super.setY(y);
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setHeight(height);
    }
}
