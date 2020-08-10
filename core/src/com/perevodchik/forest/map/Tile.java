package com.perevodchik.forest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.perevodchik.forest.enums.TileType;

import java.util.UUID;

public class Tile extends StaticTiledMapTile {
  private TileType tileType;
  private Body body = null;
  private UUID uuid = UUID.randomUUID();
  private Room room = null;
  private boolean isDoor = false;
  private boolean isRoom = false;
  private boolean isMarked = false;
  private int x;
  private int y;

  public Tile(TextureRegion textureRegion, int x, int y) {
    super(textureRegion);
    this.x = x;
    this.y = y;
  }

  public Tile(TextureRegion textureRegion, Body body, int x, int y) {
    super(textureRegion);
    this.body = body;
    this.x = x;
    this.y = y;
  }

  public boolean isRoom() {
    return isRoom;
  }

  public void setRoom(boolean room) {
    isRoom = room;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setTileType(TileType tileType) {
    this.tileType = tileType;
  }

  public TileType getTileType() {
    return tileType;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Room getRoom() {
    return room;
  }

  public void setMarked(boolean marked) {
    isMarked = marked;
  }

  public boolean isMarked() {
    return isMarked;
  }

  public void setDoor(boolean door) {
    isDoor = door;
  }

  public boolean isDoor() {
    return isDoor;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public Body getBody() {
    return body;
  }

  public UUID getUuid() {
    return uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tile)) return false;
    Tile tile = (Tile) o;
    return getUuid().equals(tile.getUuid());
  }
}
