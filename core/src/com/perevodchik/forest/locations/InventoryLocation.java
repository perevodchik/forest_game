package com.perevodchik.forest.locations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.ui.inventory.BasicInventoryContainer;
import com.perevodchik.forest.ui.inventory.DescriptionContainer;
import com.perevodchik.forest.ui.inventory.EquipmentInventoryContainer;

public class InventoryLocation extends Location {
    private TextButton toDungeonButton;
    private BitmapFont font;
    private Texture background;
    private BasicInventoryContainer basicInventoryContainer;
    private EquipmentInventoryContainer equipmentInventoryContainer;
    private DescriptionContainer descriptionContainer;
    private static ItemStack selectedStack = ItemStack.empty();

    public InventoryLocation(GameStateManager gsm, World world) {
        super(gsm, world);
        font = new BitmapFont();
        int marginX = (int) (ForestGameScreen.width * 0.05), marginY = (int) (ForestGameScreen.height * 0.05);

        {
//            basicInventoryContainer = new BasicInventoryContainer(this);
//            getStage().addActor(basicInventoryContainer);
        }

        {
//            equipmentInventoryContainer = new EquipmentInventoryContainer(this);
//            getStage().addActor(equipmentInventoryContainer);
        }

        {
//            descriptionContainer = new DescriptionContainer(
//                    this,
//                    ForestGameScreen.width / 2 + (marginX * 2) + Math.round(basicInventoryContainer.getWidth()),
//                    marginY,
//                    ForestGameScreen.blockXS * 5,
//                    ForestGameScreen.height - marginY * 2);
//            getStage().addActor(descriptionContainer);
        }

        {
            FileHandle fontFile = Gdx.files.internal("fonts/Roboto-Regular.ttf");
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 50;
            font = generator.generateFont(parameter);
            parameter.size = 40;
            generator.dispose();
        }

        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
            toDungeonButton = new TextButton("Back", textButtonStyle);

            int buttonSize = (int) (ForestGameScreen.height * 0.05);

            int dungeonX = (int) (ForestGameScreen.width * 0.05);
            int dungeonY = (int) (ForestGameScreen.height * 0.95 - toDungeonButton.getHeight());
            toDungeonButton.setX(dungeonX);
            toDungeonButton.setY(dungeonY);
            toDungeonButton.setWidth(buttonSize);
            toDungeonButton.setHeight(buttonSize);
            toDungeonButton.addListener(new CloseInventoryListener());
            getStage().addActor(toDungeonButton);
        }

        background = new Texture("tiles.png");
        if(ForestGameScreen.isDebug)
            getStage().setDebugAll(true);
    }

    public void displayItems() {
        basicInventoryContainer.displayItems();
    }

    public DescriptionContainer getDescriptionContainer() {
        return descriptionContainer;
    }

    public EquipmentInventoryContainer getEquipmentInventoryContainer() {
        return equipmentInventoryContainer;
    }

    public ItemStack getSelectedStack() {
        return selectedStack;
    }

    public void selectStack(ItemStack stack) {
        selectedStack.set(stack);
        descriptionContainer.setItemStack(stack);
    }

    @Override
    protected void handleInput() { }

    @Override
    public void update(float dt) { }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, 0, ForestGameScreen.width, ForestGameScreen.height);
        batch.end();
    }

    @Override
    public void dispose() { }

    @Override
    public void generateMap() { }

    private static class CloseInventoryListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }
        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            GameStateManager.getGameStateManager().pop();
            ForestGameScreen.isPause = false;
        }
    }

    public static class SlotController extends InputListener {
        private Slot slot;
        private Window window;

        public SlotController(Slot slot, Window window) {
            this.slot = slot;
//            this.location = location;
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            Slot.setSelectedSlot(slot);
//            window.getDescriptionContainer().setItemStack(slot.getStack());
//            window.selectStack(slot.getStack());
            return true;
        }
    }
}
