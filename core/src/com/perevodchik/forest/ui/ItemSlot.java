package com.perevodchik.forest.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.items.root.ItemStack;

public class ItemSlot extends Slot {
    // for draw texture
    private com.perevodchik.forest.enums.Slot slot;
    private static final Texture background = new Texture("stone.png");

    public ItemSlot(int index, com.perevodchik.forest.enums.Slot slot, ItemStack stack, BitmapFont font, int x, int y, int width, int height) {
        super(stack, font, x, y, width, height);
        this.slot = slot;
        if(ForestGameScreen.isDebug)
            debug();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
//        if(stack.item() != RegistryManager.empty)
//            font.draw(batch, slot.toString(), x, descriptionY);
    }
}
