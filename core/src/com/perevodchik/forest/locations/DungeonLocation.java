package com.perevodchik.forest.locations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator;
import com.perevodchik.forest.collisions.CollisionListener;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.EntityItem;
import com.perevodchik.forest.entity.EntityJuju;
import com.perevodchik.forest.entity.EntityWall;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.enums.RoomType;
import com.perevodchik.forest.enums.TileType;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.map.Room;
import com.perevodchik.forest.map.Tile;
import com.perevodchik.forest.registry.RegistryManager;
import com.perevodchik.forest.ui.AttackButton;
import com.perevodchik.forest.ui.DeathWindow;
import com.perevodchik.forest.ui.NextStageButton;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.hud.Hud;
import com.perevodchik.forest.ui.jo.Jo;
import com.perevodchik.forest.utils.FontUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DungeonLocation extends Location {
    private static final int tileSize = 32;
    private long tick = 0;
    private List<Room> rooms = new ArrayList<>();
    private final DungeonGenerator dungeonGenerator;
    private CollisionListener collisionListener;
    private static final Random random = new Random();
    private static AttackButton attackButton = null;
    private static Jo jo = null;
    private static Hud hud = null;
    private static NextStageButton nextStageButton = null;
    private static DeathWindow dialog = null;
    private Tile door;
    private LocationData locationData;

    public DungeonLocation(GameStateManager gsm, World world, LocationData locationData) {
        super(gsm, world);
        this.locationData = locationData;
        dungeonGenerator = new DungeonGenerator();
        generateMap();
        collisionListener = new CollisionListener();
        world.setContactListener(collisionListener);
        initUI();

        Gdx.app.error("STAGE", "load stage [" + toString() + "]");
    }

    @Override
    public String toString() {
        return "DungeonLocation{" +
                "stage [" + locationData.getStage() +
                "], rooms [" + rooms.size() +
                "], gridSize [" + locationData.getGridSize() +
                "], minRoomSize [" + locationData.getRoomSize().min +
                "], maxRoomSize [" + locationData.getRoomSize().max +
                "], door [" + door.getX() + ", " + door.getY() +
                "], actorsToBeAdded [" + actorsToBeAdded.size() +
                "], actors [" + actors.size() +
                "]}";
    }

    private void initUI() {
        if(attackButton == null)
            attackButton = new AttackButton(new Texture("knife0.png"), Player.getPlayer());
        if(jo == null)
            jo = new Jo(new Texture("circle.png"), new Texture("circle.png"));
        if(hud == null)
            hud = new Hud(new Texture("bag.png"), 50);
        if(nextStageButton == null)
            nextStageButton = new NextStageButton();
        getStage().addActor(attackButton);
        getStage().addActor(jo);
        getStage().addActor(hud);
        getStage().addActor(nextStageButton);
    }

    public void nextStage(LocationData locationData) {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        System.out.println(Player.getPlayer().getBody().getUserData());
        for(Body body: bodies) {
            if(body.getUserData() == null) {
                getToBeRemovedBodies().add(body);
            }
            else {
                Entity e = (Entity) body.getUserData();
                if(!(e instanceof Player))
                    getToBeRemovedBodies().add(e.getBody());
            }
        }
        locationData.setGoldCost((int) (locationData.getGoldCost() + (random.nextFloat() * (random.nextInt(10) + 1))));
        Location newLocation = new DungeonLocation(gsm, world, LocationData.prepareNextStage(locationData));
        gsm.set(newLocation);
    }

    public void nextStage() {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        System.out.println(Player.getPlayer().getBody().getUserData());
        for(Body body: bodies) {
            if(body.getUserData() == null) {
                getToBeRemovedBodies().add(body);
            }
            else {
                Entity e = (Entity) body.getUserData();
                if(!(e instanceof Player))
                    getToBeRemovedBodies().add(e.getBody());
            }
        }
        Location newLocation = new DungeonLocation(gsm, world, LocationData.prepareNextStage(locationData));
        gsm.set(newLocation);
    }

    private void dropItems(Room room) {
        int r;
        for(int c = 0; c < random.nextInt(11) + 2; c++) {
            r = random.nextInt(locationData.getStage()) + 1;
            float x = room.getX() + (random.nextFloat() + random.nextInt(room.getWidth()));
            float y = room.getY()  + (random.nextFloat() + random.nextInt(room.getHeight()));
            ItemStack stack = new ItemStack(RegistryManager.getInstance().getItemByName("golden_coin"), r);
            EntityItem item = new EntityItem(stack, world, 3, 3, BodyDef.BodyType.DynamicBody);
            item.getBody().setTransform(x, y, 0);
            item.setLocation(this);
            getActorsToBeAdded().add(item);
        }
//        spawnMobs(room);
    }

    private void spawnMobs(Room room) {
        int mobsCount = random.nextInt(room.getHeight() >= room.getWidth() ? room.getWidth() : room.getWidth());

        for(int c = 0; c < mobsCount; c++) {
            Entity juju = new EntityJuju(world, 4, 4, BodyDef.BodyType.DynamicBody, new Texture("model.png"));
            juju.setLocation(this);
            int     pX = random.nextInt(room.getWidth()) + room.getX(),
                    pY = random.nextInt(room.getHeight()) + room.getY();
            juju.getBody().setTransform(pX, pY , 0);
            getActorsToBeAdded().add(juju);
        }
    }

    @Override
    public void generateMap() {
        Grid grid = new Grid(locationData.getGridSize());
//        dungeonGenerator.setWindingChance(.5f);
        dungeonGenerator.setMaxRoomSize(locationData.getRoomSize().max);
        dungeonGenerator.setMinRoomSize(locationData.getRoomSize().min);
        dungeonGenerator.setMaxRoomsAmount(locationData.getMaxRoomAmount());
        for(com.github.czyzby.noise4j.map.generator.room.RoomType roomType: locationData.getRoomTypes())
            dungeonGenerator.addRoomType(roomType);
        dungeonGenerator.setRoomGenerationAttempts(66);
//        dungeonGenerator.setTolerance(1);
//        dungeonGenerator.setDeadEndRemovalIterations(10);
        dungeonGenerator.generate(grid);

        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(locationData.getGridSize(), locationData.getGridSize() , tileSize, tileSize);
        Texture tiles = new Texture(Gdx.files.internal("tiles_0.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, tileSize, tileSize);

        // set tile types
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                final float cell = grid.get(x, y);

                Tile tile = null;
                TiledMapTileLayer.Cell tileCell = new TiledMapTileLayer.Cell();
                if (cell == 0.0) {
                    tile = new Tile(splitTiles[0][3], x, y);
                    tile.setTileType(TileType.CORRIDOR);
                } else if (cell == 0.5) {
                    tile = new Tile(splitTiles[0][3], x, y);
                    tile.setTileType(TileType.ROOM);
                } else if (cell == 1.0) {
                    tile = new Tile(splitTiles[3][0], x, y);
                    tile.setTileType(TileType.EMPTY);
                }
                if(tile != null)
                    tileCell.setTile(tile);

                layer.setCell(x, y, tileCell);
            }
        }

        // set walls
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {

                Tile tile = (Tile) layer.getCell(x, y).getTile();
                if (tile.getTileType() != TileType.CORRIDOR && tile.getTileType() != TileType.ROOM) {
                    continue;
                }
                if (x > 0) {
                    Tile tile_xm0 = (Tile) layer.getCell(x - 1, y).getTile();
                    if (tile_xm0 != null) {
                        if (tile_xm0.getTileType() == TileType.EMPTY) {
                            tile_xm0.setTextureRegion(splitTiles[1][0]);
                            setBodyToTile(tile_xm0, x - 1, y);
                        }
                    }

                    if (y > 0) {
                        Tile tile_xm0_ym0 = (Tile) layer.getCell(x - 1, y - 1).getTile();
                        if (tile_xm0_ym0 != null) {
                            if (tile_xm0_ym0.getTileType() == TileType.EMPTY) {
                                tile_xm0_ym0.setTextureRegion(splitTiles[1][0]);
                                setBodyToTile(tile_xm0_ym0, x - 1, y - 1);
                            }
                        }

                        Tile tile_ym0 = (Tile) layer.getCell(x, y - 1).getTile();
                        if (tile_ym0 != null) {
                            if (tile_ym0.getTileType() == TileType.EMPTY) {
                                tile_ym0.setTextureRegion(splitTiles[1][0]);
                                setBodyToTile(tile_ym0, x, y - 1);
                            }
                        }

                    }
                }
                if (x < grid.getWidth()) {
                    Tile tile_xp0 = (Tile) layer.getCell(x + 1, y).getTile();
                    if (tile_xp0 != null) {
                        if (tile_xp0.getTileType() == TileType.EMPTY) {
                            tile_xp0.setTextureRegion(splitTiles[1][0]);
                            setBodyToTile(tile_xp0, x + 1, y);
                        }
                    }

                    if (y < grid.getHeight()) {
                        Tile tile_xp0_yp0 = (Tile) layer.getCell(x + 1, y + 1).getTile();
                        if (tile_xp0_yp0 != null) {
                            if (tile_xp0_yp0.getTileType() == TileType.EMPTY) {
                                tile_xp0_yp0.setTextureRegion(splitTiles[1][0]);
                                setBodyToTile(tile_xp0_yp0, x + 1, y + 1);
                            }
                        }

                        Tile tile_ym0 = (Tile) layer.getCell(x, y + 1).getTile();
                        if (tile_ym0 != null) {
                            if (tile_ym0.getTileType() == TileType.EMPTY) {
                                tile_ym0.setTextureRegion(splitTiles[1][0]);
                                setBodyToTile(tile_ym0, x, y + 1);
                            }
                        }
                    }
                }

                if (x > 0 && y < grid.getHeight()) {
                    Tile tile_xm0_yp0 = (Tile) layer.getCell(x - 1, y + 1).getTile();
                    if (tile_xm0_yp0 != null) {
                        if (tile_xm0_yp0.getTileType() == TileType.EMPTY) {
                            tile_xm0_yp0.setTextureRegion(splitTiles[1][0]);
                            setBodyToTile(tile_xm0_yp0, x - 1, y + 1);
                        }
                    }
                }

                if (x < grid.getWidth() && y > 0) {
                    Tile tile_xp0_ym0 = (Tile) layer.getCell(x + 1, y - 1).getTile();
                    if (tile_xp0_ym0 != null) {
                        if (tile_xp0_ym0.getTileType() == TileType.EMPTY) {
                            tile_xp0_ym0.setTextureRegion(splitTiles[1][0]);
                            setBodyToTile(tile_xp0_ym0, x + 1, y - 1);
                        }
                    }
                }

            }
        }
        rooms.clear();
        // find rooms
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile = (Tile) layer.getCell(x, y).getTile();

                if(tile.getTileType() != TileType.ROOM || tile.isMarked()) continue;
                tile.setMarked(true);

                int roomW = 0, roomH = 0;
                int tmpX = x, tmpY = y;

                while(tmpX < grid.getWidth()) {
                    Tile tmpTile = (Tile) layer.getCell(tmpX, y).getTile();
                    if(tmpTile.getTileType() == TileType.ROOM) {
                        tmpTile.setMarked(true);
                        roomW++;
                    } else break;
                    tmpX++;
                }
                while(tmpY < grid.getHeight()) {
                    Tile tmpTile = (Tile) layer.getCell(x, tmpY).getTile();
                    if(tmpTile.getTileType() == TileType.ROOM) {
                        tmpTile.setMarked(true);
                        roomH++;
                    } else break;
                    tmpY++;
                }

                for(int x0 = x; x0 < x + roomW; x0++) {
                    for(int y0 = y; y0 < y + roomH; y0++) {
                        Tile tmpTile = (Tile) layer.getCell(x0, y0).getTile();
                        tmpTile.setMarked(true);
                    }
                }
                Room room = new Room(x, y, roomW, roomH);
                rooms.add(room);
            }
        }

        // set spawn room
        if(!rooms.isEmpty()) {
            Room minRoom = null;
            Room maxRoom = null;
            for(Room r: rooms) {
                {
                    if(minRoom == null)
                        minRoom = r;
                    else
                    if(r.getSize() < minRoom.getSize())
                        minRoom = r;
                }
                {
                    if(maxRoom == null)
                        maxRoom = r;
                    else
                    if(r.getSize() > maxRoom.getSize())
                        maxRoom = r;
                }
            }

            if (minRoom.getRoomId().equals(maxRoom.getRoomId())) {
                maxRoom.setType(RoomType.RAID_ROOM);
                int pX = (maxRoom.getWidth() / 2 + maxRoom.getX()), pY = (maxRoom.getHeight() / 2 + maxRoom.getY());
                setDoor(layer, maxRoom);
                Player.getPlayer().getBody().setTransform(pX, pY, 0);
            } else {
                minRoom.setType(RoomType.SPAWN_ROOM);
                int pX = (minRoom.getWidth() / 2 + minRoom.getX()), pY = (minRoom.getHeight() / 2 + minRoom.getY());
                Player.getPlayer().getBody().setTransform(pX, pY, 0);

                maxRoom.setType(RoomType.BOSS_ROOM);

                int doorX = 0, doorY = 0;
                if (rooms.size() > 2) {
                    Room room = minRoom;
                    while(room.getType() == RoomType.BOSS_ROOM || room.getType() == RoomType.SPAWN_ROOM) {
                        room = rooms.get(random.nextInt(rooms.size()));
                    }
                    System.out.println(room.toString());
                    room.setType(RoomType.EXIT_ROOM);
                    doorX = random.nextInt(room.getWidth()) + room.getX();
                    doorY = random.nextInt(room.getHeight()) + room.getY();
                } else if (rooms.size() == 2) {
                    doorX = random.nextInt(maxRoom.getWidth()) + maxRoom.getX();
                    doorY = random.nextInt(maxRoom.getHeight()) + maxRoom.getY();
                }
                setDoor(layer, doorX, doorY);
            }
        }

        for(Room r: rooms) {
            boolean playerInThisRoom = r.inRoom(Player.getPlayer().getBody().getPosition());
            if(!playerInThisRoom)
                spawnMobs(r);
        }
        Player.getPlayer().setLocation(this);
        getActorsToBeAdded().add(Player.getPlayer());

        layers.add(layer);
        setMap(map);
    }

    private void setDoor(TiledMapTileLayer layer, int x, int y) {
        Tile doorTile = (Tile) layer.getCell(x, y).getTile();
        doorTile.setDoor(true);
        Texture doorTexture = new Texture(Gdx.files.internal("floor.png"));
        TextureRegion[][] doorTextureRegion = TextureRegion.split(doorTexture, tileSize, tileSize);
        doorTile.setTextureRegion(doorTextureRegion[0][0]);
        door = doorTile;
    }

    private void setDoor(TiledMapTileLayer layer, Room room) {
        int doorX, doorY;
        doorX = random.nextInt(room.getWidth()) + room.getX();
        doorY = random.nextInt(room.getHeight()) + room.getY();
        Tile doorTile = (Tile) layer.getCell(doorX, doorY).getTile();
        doorTile.setDoor(true);
        Texture doorTexture = new Texture(Gdx.files.internal("floor.png"));
        TextureRegion[][] doorTextureRegion = TextureRegion.split(doorTexture, tileSize, tileSize);
        doorTile.setTextureRegion(doorTextureRegion[0][0]);
        door = doorTile;
    }

    // create oval body for location actor (eq tile, wall, chest, decor)
    private void setBodyToTileOval(Tile tile) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        CircleShape poly = new CircleShape();
        poly.setRadius(8 * ForestGameScreen.UNIT_SCALE);
        body.createFixture(poly, 100);
        poly.dispose();

//        tile.setBody(body);
        body.setTransform((16 + tileSize * tile.getX()) * ForestGameScreen.UNIT_SCALE, (16 + tileSize * tile.getY()) * ForestGameScreen.UNIT_SCALE, 0);
    }

    // create rectangle body for location actor (eq tile, wall, chest, decor)
    private EntityWall setBodyToTile(Tile tile, int x, int y) {
        EntityWall wall;
        tile.setTileType(TileType.WALL);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body tileBody = world.createBody(bodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(16 * ForestGameScreen.UNIT_SCALE, 16 * ForestGameScreen.UNIT_SCALE);
        tileBody.createFixture(poly, 100);
        poly.dispose();

        tileBody.setTransform((16 + tileSize * x) * ForestGameScreen.UNIT_SCALE, (16 + tileSize * y) * ForestGameScreen.UNIT_SCALE, 0);

        wall = new EntityWall(world, tileBody, tile);
        spawnEntity(wall);

        return wall;
    }

    @Override
    protected void handleInput() { }

    @Override
    public void update(float dt) {
        if(ForestGameScreen.isPause) return;
        if(ForestGameScreen.isPlayerDead) {
            if (dialog == null) {
                jo.setVisible(false);
                hud.setVisible(false);
                nextStageButton.setVisible(false);
                attackButton.setVisible(false);

                Padding padding = new Padding();
                dialog = new DeathWindow(padding, ForestGameScreen.blockX * 3, ForestGameScreen.blockY * 3);
                dialog.title("Game Over!");
                dialog.text("You can continue game for");
                dialog.text(locationData.getGoldCost() + " Gold!");

                TextButton.TextButtonStyle style1 = new TextButton.TextButtonStyle();
                style1.font = FontUtil.generate();
                Button button = new TextButton("Continue", style1);
                button.addListener(new ContinueGameListener());
                dialog.button(button, 0);

                Button button0 = new TextButton("New Game", style1);
                button0.addListener(new NewGameListener());
                dialog.button(button0, 1);
                dialog.show();
                getStage().addActor(dialog);
            }
        }
        if(tick % 322 == 0) {
            int rId = random.nextInt(RegistryManager.idIndex - 1) + 1;
            ItemStack stack = new ItemStack(RegistryManager.getInstance().getItemById(rId), 1);
//            ItemStack stack = new ItemStack(RegistryManager.getInstance().getItemByName("moon_armor"), 1);
            if(stack.item() != null) {
                Room room = rooms.get(random.nextInt(rooms.size()));
                float x = room.getX() + (random.nextFloat() + random.nextInt(room.getWidth()));
                float y = room.getY()  + (random.nextFloat() + random.nextInt(room.getHeight()));
                EntityItem item = new EntityItem(stack, world, 3, 3, BodyDef.BodyType.DynamicBody);
                item.getBody().setTransform(x, y, 0);
                item.setLocation(this);
                getActorsToBeAdded().add(item);
            }
        }
        Iterator<Entity> entityToBeAdded = getActorsToBeAdded().iterator();
        while (entityToBeAdded.hasNext()) {
            Entity e = entityToBeAdded.next();
            actors.add(e);
            entityToBeAdded.remove();
            if(e instanceof EntityItem)
                ((EntityItem) e).setCanTake(true);
        }
        for (Entity e : actors)
            e.update(dt);
        tick++;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (jo.isTouch()) {
            float x = jo.getValueX();
            float y = jo.getValueY();
            Player.getPlayer().setVelocity(x, y);
        } else {
            if(Player.getPlayer().isMoving()) {
                Player.getPlayer().setVelocity(0, 0);
            }
        }
        batch.begin();
        for (Entity a : actors)
            a.render(batch);
        batch.end();
        Player.canGoNextStage =
                Player.getPlayer().getBody().getPosition().x > door.getX() &&
                Player.getPlayer().getBody().getPosition().x < door.getX() + 1 &&
                Player.getPlayer().getBody().getPosition().y > door.getY() &&
                Player.getPlayer().getBody().getPosition().y < door.getY() + 1;
    }

    @Override
    public void dispose() { }

    public List<Room> getRooms() {
        return rooms;
    }

    private class NewGameListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.error("button", "newGame");
            dialog.hide();
            dialog = null;
            nextStage(LocationData.init());
            ForestGameScreen.isPlayerDead = false;
            ForestGameScreen.isPause = true;
            Player.getPlayer().dropItems();

            jo.setVisible(true);
            hud.setVisible(true);
            nextStageButton.setVisible(true);
            attackButton.setVisible(true);
            return true;
        }
    }

    private class ContinueGameListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.error("button", "continue");
            dialog.hide();
            dialog = null;
            ForestGameScreen.isPlayerDead = false;
            ForestGameScreen.isPause = true;
            ItemStack gold = new ItemStack(RegistryManager.goldenCoin, locationData.getGoldCost());
            Player.getPlayer().getBasicInventory().remove(gold);

            jo.setVisible(true);
            hud.setVisible(true);
            nextStageButton.setVisible(true);
            attackButton.setVisible(true);
            return true;
        }
    }
}