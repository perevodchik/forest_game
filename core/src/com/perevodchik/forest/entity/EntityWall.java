package com.perevodchik.forest.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.map.Tile;

public class EntityWall extends Entity {
    private Tile tile;

    public EntityWall(World world, Body body, Tile tile) {
        super(world, body);
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public void generateTextures() { }

    @Override
    public void render(SpriteBatch batch) { }

    @Override
    public void update(float dt) { }
}
