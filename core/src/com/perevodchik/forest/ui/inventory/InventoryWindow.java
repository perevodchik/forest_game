package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.utils.FontUtil;

public class InventoryWindow extends Window {
    private BasicInventoryContainer basicContainer;
    private EquipmentInventoryContainer equipmentContainer;
    private DescriptionContainer descriptionContainer;
    private Button backButton;
    private static ItemStack selectedStack = ItemStack.empty();
    private int marginX, marginY;

    public InventoryWindow(Padding padding, float x, float y, int width, int height) {
        super(padding, x, y, width, height);
    }

    public InventoryWindow(Padding padding, int width, int height) {
        super(padding, width, height);
    }

    @Override
    public void initUI() {
        marginX = (int) (ForestGameScreen.width * 0.05);
        marginY = (int) (ForestGameScreen.height * 0.05);
        basicContainer = new BasicInventoryContainer(this, new Padding(), 0);
        equipmentContainer = new EquipmentInventoryContainer(this);
        descriptionContainer = new DescriptionContainer(
                this,
                Math.round(ForestGameScreen.width / 1.55f),
                marginY,
                ForestGameScreen.blockXS * 5,
                ForestGameScreen.height - marginY * 2);
        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = FontUtil.generate(50);
        backButton = new TextButton("Back", backButtonStyle);
        backButton.addListener(new CloseInventoryController(this));
        backButton.setX(marginX);
        backButton.setY(getHeight() - backButton.getHeight() - marginY);

        addActor(basicContainer);
        addActor(equipmentContainer);
        addActor(descriptionContainer);
//        addActor(backButton);

        if(ForestGameScreen.isDebug)
            debugAll();
    }

    public void displayItems() {
        basicContainer.displayItems();
    }

    public DescriptionContainer getDescriptionContainer() {
        return descriptionContainer;
    }

    public EquipmentInventoryContainer getEquipmentInventoryContainer() {
        return equipmentContainer;
    }

    public ItemStack getSelectedStack() {
        return selectedStack;
    }

    public void selectStack(ItemStack stack) {
        selectedStack.set(stack);
        descriptionContainer.setItemStack(stack);
    }

    public static class CloseInventoryController extends InputListener {
        private Window window;

        public CloseInventoryController(Window window) {
            this.window = window;
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            GameStateManager.getGameStateManager().peek().hideWindow(window);
            ForestGameScreen.isPause = false;
            return true;
        }
    }

    public static class SlotController extends InputListener {
        private Slot slot;
        private Window window;

        public SlotController(Slot slot, Window window) {
            this.slot = slot;
            this.window = window;
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            Slot.setSelectedSlot(slot);
            if(window != null) {
                Gdx.app.error("window", "not null");
                ((InventoryWindow) window).getDescriptionContainer().setItemStack(slot.getStack());
                ((InventoryWindow) window).selectStack(slot.getStack());
            } else
                Gdx.app.error("window", "null");
            return true;
        }
    }
}
