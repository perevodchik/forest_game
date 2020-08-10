package com.perevodchik.forest.ui.jo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Jo extends Actor {
    private Texture circle;
    private Texture cursor;
    private boolean isTouch = false;
    private float radius = 160;
    private float inverseRadius = 1 / radius;
    private static final float cursorRadius = 40;
    private float   cursorX = 0,
                    cursorY = 0;
    private float   valueX = 0,
                    valueY = 0;
    private List<IJoChangeListener> listeners = new ArrayList<>();

    public Jo(Texture texture0, Texture texture1) {
        this.circle = texture0;
        this.cursor = texture1;
        setWidth(radius * 2);
        setHeight(radius * 2);
        setX(100);
        setY(100);
        boolean listenerAdded = addListener(new JoController(this));
    }

    public boolean isTouch() {
        return isTouch;
    }

    public float getValueX() {
        return valueX;
    }

    public float getValueY() {
        return valueY;
    }

    public void handle(float x, float y) {
        for(IJoChangeListener listener: listeners) {
            listener.change(x, y);
        }
    }

    public void addListener(IJoChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IJoChangeListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void resetCursor() {
        cursorX = 0;
        cursorY = 0;
    }

    public void changeCursor(float x, float y) {
        float dx = x - radius;
        float dy = y - radius;
        float length = (float)Math.sqrt(dx * dx + dy * dy);
        if(length < radius) {
            cursorX = dx;
            cursorY = dy;
        } else {
            float k =  radius / length;
            cursorX = dx * k;
            cursorY = dy * k;
        }
        valueX = cursorX * inverseRadius;
        valueY = cursorY * inverseRadius;
    }

    public void joTouch() {
        System.out.println("Jo touch");
        isTouch = true;
    }

    public void joUnTouch() {
        resetCursor();
//        Player.getPlayer().setVelocity(0, 0);
        isTouch = false;
    }

    @Override
    public Actor hit (float x, float y, boolean touchable) {
        if(ForestGameScreen.isPause) return null;
        Actor actor = super.hit(x, y, touchable);
        if(actor == null) return null;
        float dx = x - radius;
        float dy = y - radius;
        return (dx * dx + dy * dy <= radius * radius) ? this : null;
    }

    @Override
    public void setWidth(float w) {
        super.setWidth(w);
        super.setHeight(w);
        radius = w / 2;
        inverseRadius = 1 / radius;
    }

    @Override
    public void setHeight(float h) {
        super.setWidth(h);
        super.setHeight(h);
        radius = h / 2;
        inverseRadius = 1 / radius;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(ForestGameScreen.isPause) return;
        super.draw(batch, parentAlpha);
        batch.draw(circle, getX(), getY(), getWidth(), getHeight());
        if(isTouch) {
            batch.draw(cursor,
                    getX() + radius - cursorRadius + cursorX,
                    getY() + radius - cursorRadius + cursorY,
                    2 * cursorRadius,
                    2 * cursorRadius);
        } else {
            batch.draw(cursor,
                    getX() + radius - cursorRadius,
                    getY() + radius - cursorRadius,
                    2 * cursorRadius,
                    2 * cursorRadius);
        }
    }
}
