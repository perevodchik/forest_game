package com.perevodchik.forest.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;

public class Slot extends Actor {
    protected ItemStack stack;
    protected BitmapFont font;
    protected int x, y;
    private int width, height;
    private int selectedX, selectedY;
    private int selectedWidth, selectedHeight;
    protected int descriptionY;
    private static Slot selectedSlot = null;
    private boolean isSelected = false;
//    private ShapeRenderer shapeRenderer;

    public Slot(ItemStack stack, BitmapFont font, int x, int y, int width, int height) {
        this.stack = stack;
        this.font = font;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.x = (int) (x + width * 0.05f);
        this.y = (int) (y + height * 0.05f);
        this.width = (int) (width * 0.9f);
        this.height = (int) (height * 0.9f);
        this.descriptionY = (int) (y + height * 0.3f);
        this.selectedX = (int) (x + width * 0.1f);
        this.selectedY = (int) (y + height * 0.1f);
        this.selectedWidth = (int) (width * 0.75f);
        this.selectedHeight = (int) (height * 0.75f);
        if(ForestGameScreen.isDebug)
            debug();

//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setAutoShapeType(true);
//        shapeRenderer.setColor(Color.GREEN);
    }

    public static void setSelectedSlot(Slot selectedSlot) {
        if(Slot.selectedSlot != null) {
            Slot.selectedSlot.setSelected(false);
        }
        Slot.selectedSlot = selectedSlot;
        selectedSlot.setSelected(true);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (stack.item() != RegistryManager.empty) {
            if(isSelected) {
                batch.draw(stack.item().getTexture(), x, y, width, height);
//                batch.end();
//                shapeRenderer.begin();
//                shapeRenderer.rect(x, y, width, height);
//                shapeRenderer.end();
//                batch.begin();
            }
            else
                batch.draw(stack.item().getTexture(), selectedX, selectedY, selectedWidth, selectedHeight);
            if(stack.item().isCanStack())
                font.draw(batch, stack.getCount() + "", x, descriptionY);
        }
    }
}
