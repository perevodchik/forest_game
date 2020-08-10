package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.ui.blacksmith.BlacksmithWindow;
import com.perevodchik.forest.utils.FontUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class BasicInventoryContainer extends Window {
    private Slot[][] slots;
    private TextButton nextPageButton;
    private TextButton prevPageButton;
    private Padding padding;
//    private final InventoryLocation location;
//    private final Window window;
    private int inventoryPage = 0;
    private static final int inventorySlotsOnPage = 15;
    private boolean canNext = false;
    private boolean canBack = false;
    private int listenerClassType;
    private Window parent;

    public BasicInventoryContainer(Window parent, Padding padding, float x, float y, int width, int height, int listenerClassType) {
        super(padding, x, y, width, height);
        this.parent = parent;
        this.listenerClassType = listenerClassType;
    }

    public BasicInventoryContainer(Window parent, Padding padding, int width, int height, int listenerClassType) {
        super(padding, width, height);
        this.parent = parent;
        this.listenerClassType = listenerClassType;
    }

    public BasicInventoryContainer(Window parent, Padding padding, int listenerClassType) {
        super(padding);
        this.parent = parent;
        this.listenerClassType = listenerClassType;
//        setX((int) ((4 * 15) + (4 * ForestGameScreen.blockXS) + (ForestGameScreen.height * 0.05)));
    }

    @Override
    public void initUI() {
        slots = new Slot[3][5];
        BitmapFont font = FontUtil.generate(40);
        int marginX = (int) (ForestGameScreen.width * 0.05), marginY = (int) (ForestGameScreen.height * 0.05);
        int buttonSize = (int) (ForestGameScreen.height * 0.05);
        int prevX = 50;
        int prevY = ForestGameScreen.height - (ForestGameScreen.blockXS * 4);
        int nextX =  (int) (ForestGameScreen.width * 0.05) + (ForestGameScreen.blockXS * 5 + (15 * 4) + ForestGameScreen.blockXS + marginX);
        int nextY = ForestGameScreen.height - (ForestGameScreen.blockXS * 4);

        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
            nextPageButton = new TextButton("Next", textButtonStyle);
            prevPageButton = new TextButton("Prev", textButtonStyle);

            prevPageButton.setX(prevX);
            prevPageButton.setY(prevY);
            prevPageButton.setWidth(buttonSize);
            prevPageButton.setHeight(buttonSize);
            prevPageButton.addListener(new PrevInventoryListener());
            addActor(prevPageButton);

            nextPageButton.setX(nextX);
            nextPageButton.setY(nextY);
            nextPageButton.setWidth(buttonSize);
            nextPageButton.setHeight(buttonSize);
            nextPageButton.addListener(new NextInventoryListener());
            addActor(nextPageButton);
        }

        {
            SlotContainer slotContainer = new SlotContainer(
                    ForestGameScreen.blockXS + marginX,
                    marginY,
                    ForestGameScreen.blockXS * 5 + (15 * 4),
                    (15 * 2) + (ForestGameScreen.blockXS * 3), true);
            for(int c = 2; c >= 0; c--) {
                for(int j = 0; j < 5; j++) {
                    int slotX = (ForestGameScreen.blockXS * j) + (15 * j);
                    int slotY = (ForestGameScreen.blockXS * c) + (15 * c);
                    Slot slot = new Slot(
                            ItemStack.empty()
                            , font,
                            slotX, slotY,
                            ForestGameScreen.blockXS, ForestGameScreen.blockXS);
                    InputListener listener = null;
                    Gdx.app.error("parent is null?", String.valueOf((parent == null)));
                    switch (listenerClassType) {
                        case 0:
                            listener = new InventoryWindow.SlotController(slot, parent);
                            break;
                        case 1:
                            listener = new BlacksmithWindow.SlotController(slot, parent);
                            break;
                    }
                    if(listener != null)
                        slot.addListener(listener);
                    slotContainer.addActor(slot);
                    if(slots != null)
                        slots[c][j] = slot;
                }
            }
            displayItems();
            addActor(slotContainer);
        }

        setHeight(nextY + nextPageButton.getHeight() + 50);
        setWidth(nextPageButton.getX() + nextPageButton.getWidth() + 50);
        setX(prevX);
        setY(50);

        if(Player.getPlayer() != null)
            if(Player.getPlayer().getBasicInventory() != null)
                if(Player.getPlayer().getBasicInventory().getItemsCount() > 15)
                    canNext = true;
    }

    public Slot[][] getSlots() {
        return slots;
    }

    public ArrayList<Slot> getSlotsAsList() {
        ArrayList<Slot> slotArrayList = new ArrayList<>();
        for (Slot[] slot : slots) {
            Collections.addAll(slotArrayList, slot);
        }
        return slotArrayList;
    }

    public void displayItems() {
        if(Player.getPlayer() != null) {
            if(Player.getPlayer().getBasicInventory() != null) {
                int itemIndex = inventoryPage * inventorySlotsOnPage;
                for(int c = 2; c >= 0; c--) {
                    for(int j = 0; j < 5; j++) {
                        if (slots != null) {
                            Slot slot = slots[c][j];
                            slot.setStack(Player.getPlayer().getBasicInventory().get(itemIndex++));
                        }
                    }
                }
            }
        }
    }

    private class PrevInventoryListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            if(!canBack) return true;
            canNext = true;
            if(inventoryPage > 0) {
                inventoryPage--;
                displayItems();
            } else canBack = false;
            return true;
        }
    }

    private class NextInventoryListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            if(!canNext) return true;
            if((inventoryPage + 1) * 15 < Player.getPlayer().getBasicInventory().getItemsCount()) {
                canBack = true;
                inventoryPage++;
                canNext = true;
                displayItems();
            }
            return true;
        }
    }


}
