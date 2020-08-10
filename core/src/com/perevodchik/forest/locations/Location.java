package com.perevodchik.forest.locations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.ui.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class Location {
    protected GameStateManager gsm;
    private TiledMap map = null;
    public final World world;
    public final List<Entity> actorsToBeAdded;
    public final List<Entity> actors;
    private static final List<Body> toBeRemovedBodies = new ArrayList<>();
    private boolean isDirty = false;
    private final Stage stage;
    private Window currentWindow = null;
    private boolean isShowWindow = false;
    private HashMap<UUID, Window> windows = new HashMap<>();

    public Location(GameStateManager gsm, World world) {
        this.gsm = gsm;
        this.world = world;
        actors = new ArrayList<>();
        actorsToBeAdded = new ArrayList<>();
        stage = new Stage();
    }

    public Stage getStage() {
        return stage;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public void spawnEntity(Entity e) {
        getActorsToBeAdded().add(e);
    }

    public Entity findEntityById(UUID id) {
        for(Entity e: actors) {
            if(e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public void removeEntity(UUID id) {
        for(Entity e: actors) {
            if(e.getId().equals(id)) {
                actors.remove(e);
                toBeRemovedBodies.add(e.getBody());
                return;
            }
        }
    }

    public void showWindow(Window window) {
        getStage().addActor(window);
        windows.put(window.getId(), window);
    }

    public void hideWindow(Window window) {
        windows.get(window.getId()).remove();
        window.remove();
    }

    public List<Body> getToBeRemovedBodies() {
        return toBeRemovedBodies;
    }

    public List<Entity> getActorsToBeAdded() {
        return actorsToBeAdded;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public boolean isDirty() {
        return isDirty;
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
    public abstract void generateMap();
}