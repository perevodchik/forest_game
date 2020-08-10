package com.perevodchik.forest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.locations.DungeonLocation;
import com.perevodchik.forest.locations.Location;
import com.perevodchik.forest.locations.LocationData;
import com.perevodchik.forest.registry.RegistryManager;

public class ForestGameScreen implements Screen {
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap map;
    private SpriteBatch batch;
    private GameStateManager gsm;
    private Stage stage;
    private World world;
    private static final Texture texture = new Texture("stone.png");
    private static int bgX, bgY;
    public static int width, height;
    public static int blockXS, blockYS;
    public static int blockX, blockY;
    public static int blockXSS, blockYSS;
    public static boolean isPlayerDead = false;
    public static boolean isPause = false;
    public static boolean isDebug = true;
//    public static boolean isDebug = false;
    public static ForestGameScreen instance = null;

    private static final float MULTIPLIER = 32f;
    private static final float WORLD_STEEP = 1 / 60f;
    public static final float UNIT_SCALE = 1f / MULTIPLIER;

    ForestGameScreen() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        blockX = (int) (width * 0.1);
        blockY = (int) (height * 0.1);
        blockXS = (int) (width * 0.07);
        blockYS = (int) (height * 0.07);
        blockXSS = (int) (width * 0.01);
        blockYSS = (int) (height * 0.01);
        bgX = -width / 2;
        bgY = -height / 2;
        RegistryManager.getInstance();

        gsm = GameStateManager.getGameStateManager();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        world = new World(new Vector2(0, 0), false);
        renderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        Player.createPlayer(world);

        Location location = new DungeonLocation(gsm, world, LocationData.init());
//        Location location = new InventoryLocation(gsm, world);
        map = location.getMap();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        if(isDebug)
            camera.setToOrtho(false, 60, 30);
//            camera.setToOrtho(false, 40, 20);
        else
            camera.setToOrtho(false, 10, 5);

        setLocation(location);

        gsm.push(location);
        if(instance == null) instance = this;
    }

    public void setLocation(Location location) {
        this.stage = location.getStage();
        this.map = location.getMap();
        Gdx.input.setInputProcessor(stage);
    }

    public static ForestGameScreen instance() {
        return instance;
    }

    @Override
    public void show() {
        System.out.println("SHOW");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(texture, bgX, bgY, width, height);
        batch.end();

        if(map != null) {
            map = gsm.getMap();
            tiledMapRenderer.setMap(map);
            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();
        }

        if(!isPause) gsm.update(delta);
        gsm.render(batch);

        if(isDebug && !isPause) renderer.render(world, camera.combined);

        stage.draw();
        stage.act(delta);
        if(map != null) {
            world.step(WORLD_STEEP, 2, 2);
            if(gsm.getToBeRemovedBodies().size() > 0)
            for (Body body : gsm.getToBeRemovedBodies()) {
                Entity e = (Entity) body.getUserData();
                if(e != null) {
                    if(e.isDead())
                        e.dropItems();
                }
                world.destroyBody(body);
            }
            gsm.getToBeRemovedBodies().clear();

            camera.position.x = Player.getPlayer().getBody().getPosition().x;
            camera.position.y = Player.getPlayer().getBody().getPosition().y;

            camera.update();
        }
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
        System.out.println("PAUSE");
    }

    @Override
    public void resume() {
        System.out.println("RESUME");
    }

    @Override
    public void hide() {
        System.out.println("HIDE");
    }

    @Override
    public void dispose() {
        System.out.println("DISPOSE");
        world.dispose();
        map.dispose();
        batch.dispose();
    }
}
