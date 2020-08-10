package com.perevodchik.forest.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.entity.Player;

public class Hud extends Actor {
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private BitmapFont fontStats;
    private BitmapFont font;
    private static float width, height;
    private static int x, y;
    private static float barWidth, barHeight;
    private static int barX, healthY, energyY;
    private static int pauseX, pauseY;
    private static Label labelPause;
    private static HudController hudController;
    private static TextButton nextStageButton;

    public Hud(Texture texture, float radius) {
        this.texture = texture;
        shapeRenderer = new ShapeRenderer();
        fontStats = new BitmapFont();
        font = new BitmapFont();
        hudController = new HudController(this);
        addListener(hudController);

        {
            FileHandle fontFile = Gdx.files.internal("fonts/Roboto-Regular.ttf");
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 50;
            fontStats = generator.generateFont(parameter);
            parameter.size = 125;
            font = generator.generateFont(parameter);
            generator.dispose();
        }

        labelPause = new Label("Pause", new Label.LabelStyle(font, Color.FOREST));
        labelPause.setPosition(ForestGameScreen.width / 2f - labelPause.getPrefWidth() / 2,ForestGameScreen.height / 2f);

        width = ForestGameScreen.height * 0.1f;
        height = ForestGameScreen.height * 0.1f;
        x = (int) (ForestGameScreen.width * 0.01);
        y = (int) ((int) (ForestGameScreen.height * 0.96) - height);

        barWidth = ForestGameScreen.width * 0.15f;
        barHeight = ForestGameScreen.height * 0.05f;
        barX = (int) ((int) (ForestGameScreen.width * 0.05) + width);
        healthY = (int) (ForestGameScreen.height * 0.95);
        energyY = (int) (healthY - (height / 2) - 5);
    }

    @Override
    public Actor hit (float x, float y, boolean touchable) {
        super.hit(x, y, touchable);
        if(x > Hud.x && x < Hud.x + width && y > Hud.y && y < Hud.y + height) {
            return this;
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(ForestGameScreen.isPause) {
            labelPause.draw(batch, parentAlpha);
        }
        // draw health bar
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.LIGHT_GRAY);
//        shapeRenderer.rect(barX, healthY, width, height);
//        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.FIREBRICK);
//        shapeRenderer.rect(barX, healthY, width, height);
//        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.DARK_GRAY);
//        shapeRenderer.rect(barX, healthY, width, height);
//        shapeRenderer.end();

        // draw energy bar
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.LIGHT_GRAY);
//        shapeRenderer.rect(barX, energyY, width, height);
//        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.CYAN);
//        shapeRenderer.rect(barX, energyY, width, height);
//        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.DARK_GRAY);
//        shapeRenderer.rect(barX, energyY, width, height);
//        shapeRenderer.end();

        batch.draw(texture, x, y, width, height);
        fontStats.setColor(Color.FIREBRICK);
        fontStats.draw(batch, ((int) Player.getPlayer().getCurrentHealth()) + "/" + ((int) Player.getPlayer().getMaxHealth()), barX, healthY);
        fontStats.setColor(Color.CYAN);
        fontStats.draw(batch, ((int) Player.getPlayer().getCurrentEnergy()) + "/" + ((int) Player.getPlayer().getMaxEnergy()), barX, energyY);
    }
}
