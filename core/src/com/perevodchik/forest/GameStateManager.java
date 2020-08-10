package com.perevodchik.forest;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.perevodchik.forest.locations.Location;

import java.util.List;
import java.util.Stack;

public class GameStateManager {
    private Stack<Location> locations;
    private static GameStateManager gameStateManager;

    private GameStateManager() {
        locations = new Stack<>();
    }

    public void push(Location location) {
        locations.push(location);
    }

    public void pop() {
        locations.pop().dispose();
        ForestGameScreen.instance().setLocation(locations.peek());
    }

    public void set(Location location) {
        locations.pop().dispose();
        locations.push(location);
        ForestGameScreen.instance().setLocation(location);
    }

    public void update(float dt) {
        locations.peek().update(dt);
    }

    public void render(SpriteBatch batch) {
        locations.peek().render(batch);
    }

    public Location peek() {
        return locations.peek();
    }

    public TiledMap getMap() {
        return locations.peek().getMap();
    }

    public static GameStateManager getGameStateManager() {
        if(gameStateManager == null) gameStateManager = new GameStateManager();
        return gameStateManager;
    }

    public List<Body> getToBeRemovedBodies() {
        return locations.peek().getToBeRemovedBodies();
    }
}