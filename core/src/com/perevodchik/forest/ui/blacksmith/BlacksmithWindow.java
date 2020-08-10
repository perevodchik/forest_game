package com.perevodchik.forest.ui.blacksmith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.GameStateManager;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.ui.inventory.BasicInventoryContainer;
import com.perevodchik.forest.ui.inventory.EquipmentInventoryContainer;
import com.perevodchik.forest.utils.FontUtil;

public class BlacksmithWindow extends Window {
    private BasicInventoryContainer basicContainer;
    private EquipmentInventoryContainer equipmentContainer;
    private Button backButton;
    private BlacksmithContainer blacksmithContainer;

    public BlacksmithWindow(Padding padding, float x, float y, int width, int height) {
        super(padding, x, y, width, height);
    }

    public BlacksmithWindow(Padding padding, int width, int height) {
        super(padding, width, height);
    }

    public BlacksmithWindow(Padding padding) {
        super(padding);
        setWidth(ForestGameScreen.width);
        setHeight(ForestGameScreen.height);
    }

    @Override
    public void initUI() {
        basicContainer = new BasicInventoryContainer(this, new Padding(), 1);
        equipmentContainer = new EquipmentInventoryContainer(this);

        blacksmithContainer = new BlacksmithContainer(new Padding(),
                ForestGameScreen.width / 1.62f,
                50,
                (int) (ForestGameScreen.width - (ForestGameScreen.width - ForestGameScreen.width / 3.2f)),
                ForestGameScreen.height - 100 );
//        blacksmithContainer.setX(ForestGameScreen.width / 1.62f);
//        blacksmithContainer.setY(50);
//        float w = ForestGameScreen.width - ForestGameScreen.width / 3.2f;
//        blacksmithContainer.setWidth(ForestGameScreen.width - w);
//        blacksmithContainer.setHeight(ForestGameScreen.height - 100);

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = FontUtil.generate(50);
        backButton = new TextButton("Back", backButtonStyle);
        backButton.addListener(new BlacksmithWindow.CloseBlacksmithController(this));
        backButton.setX(ForestGameScreen.width * 0.05f);
        backButton.setY(getHeight() - backButton.getHeight() - ForestGameScreen.width * 0.05f);

        addActor(basicContainer);
        addActor(equipmentContainer);
        addActor(blacksmithContainer);
        addActor(backButton);

        setBackground(new Texture("floor.png"));

        if(ForestGameScreen.isDebug)
            debugAll();
    }

    public BlacksmithContainer getBlacksmithContainer() {
        return blacksmithContainer;
    }

    public static class CloseBlacksmithController extends InputListener {
        private Window window;

        public CloseBlacksmithController(Window window) {
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
            ((BlacksmithWindow) window).getBlacksmithContainer().setStack(slot.getStack());
            return true;
        }
    }
}
