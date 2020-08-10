package com.perevodchik.forest.map;

import com.badlogic.gdx.math.Vector2;
import com.perevodchik.forest.enums.RoomType;

import java.util.UUID;
import java.util.Vector;

public class Room {
    private UUID roomId;
    private RoomType type;
    private int width;
    private int height;
    private int x;
    private int y;
    private RoomCell[][] cells;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        cells = new RoomCell[width][height];
        roomId = UUID.randomUUID();
    }

    public RoomCell[][] getCells() {
        return cells;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getSize() {
        return width * height;
    }

    public boolean inRoom(Vector2 v) {
        return v.x >= x && v.x < x + width && v.y >= y && v.y <= y + height;
    }

    public static class RoomCell {
        private int x;
        private int y;

        public RoomCell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "type=" + type +
                ", width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}