package com.perevodchik.forest.locations;

import com.github.czyzby.noise4j.map.generator.room.RoomType;
import com.perevodchik.forest.utils.MinMax;

import java.util.HashSet;
import java.util.Set;

public class LocationData {
    private int gridSize;
    private MinMax roomSize;
    private int stage;
    private int maxRoomAmount;
    private Set<RoomType> roomTypes;
    private int goldCost = 1;

    public LocationData(int stage, int gridSize, MinMax roomSize, int maxRoomAmount, Set<RoomType> roomTypes) {
        this.stage = stage;
        this.gridSize = gridSize;
        this.roomSize = roomSize;
        this.maxRoomAmount = maxRoomAmount;
        this.roomTypes = roomTypes;
    }

    public int getGridSize() {
        return gridSize;
    }

    public MinMax getRoomSize() {
        return roomSize;
    }

    public int getStage() {
        return stage;
    }

    public int getMaxRoomAmount() {
        return maxRoomAmount;
    }

    public Set<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public static LocationData init() {
        return new LocationData(1, 33, new MinMax(5, 11), 5, new HashSet<>());
    }

    public static LocationData prepareNextStage(LocationData data) {
        MinMax roomSize;
        int stage = data.stage + 1;
        int maxRoomAmount = data.maxRoomAmount;
        if(stage % 9 == 0) {
            if(data.roomSize.max < 21)
                roomSize = new MinMax(data.roomSize.min + 2, data.roomSize.max + 2);
            else
                roomSize = data.roomSize;
            maxRoomAmount += 1;
        } else
            roomSize = data.roomSize;

        switch (stage) {
            case 5: {
                data.getRoomTypes().add(RoomType.DefaultRoomType.CROSS);
                break;
            }
            case 10: {
                data.getRoomTypes().add(RoomType.DefaultRoomType.ROUNDED);
                break;
            }
            case 15: {
                data.getRoomTypes().add(RoomType.DefaultRoomType.SQUARE);
                break;
            }
            case 20: {
                data.getRoomTypes().add(RoomType.DefaultRoomType.DIAMOND);
                break;
            }
            case 25: {
                data.getRoomTypes().add(RoomType.DefaultRoomType.CASTLE);
                break;
            }
        }

        return new LocationData(stage, data.gridSize, roomSize, maxRoomAmount, data.getRoomTypes());
    }
}
