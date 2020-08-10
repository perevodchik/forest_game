package com.perevodchik.forest.ui.blacksmith;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.enums.Rarity;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.ui.ItemSlot;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.utils.FontUtil;

public class BlacksmithContainer extends Window {
    private Slot itemSlot;
    private Label itemLabel;
    private Label rareLabel;
    private Label costLabel;
    private ItemStack stackToUpgrade = ItemStack.empty();

    public BlacksmithContainer(Padding padding, float x, float y, int width, int height) {
        super(padding, x, y, width, height);
    }

    public BlacksmithContainer(Padding padding, int width, int height) {
        super(padding, width, height);
    }

    public BlacksmithContainer(Padding padding) {
        super(padding);
    }

    @Override
    public void initUI() {
        itemSlot = new ItemSlot(
                0,
                com.perevodchik.forest.enums.Slot.MAIN_HAND,
                ItemStack.empty(), FontUtil.generate(),
                (int) (getWidth() / 2 - ForestGameScreen.blockXS / 2), (int) (getHeight() - 50 - ForestGameScreen.blockXS),
                ForestGameScreen.blockXS, ForestGameScreen.blockXS);
        addActor(itemSlot);

        Label.LabelStyle itemLabelStyle = new Label.LabelStyle();
        itemLabelStyle.font = FontUtil.generate(55);
        itemLabelStyle.fontColor = Rarity.LEGENDARY.getColor();
        itemLabel = new Label("Moon Armor", itemLabelStyle);
        itemLabel.setY(itemSlot.getY() - 50 - itemLabel.getHeight());
        itemLabel.setX(getWidth() / 2 - itemLabel.getWidth() / 2);
        addActor(itemLabel);

        Label.LabelStyle costLabelStyle = new Label.LabelStyle();
        costLabelStyle.font = FontUtil.generate(40);
        costLabelStyle.fontColor = Color.GOLD;
        costLabel = new Label("Upgrade cost " + getUpgradeCost() + " Gold", costLabelStyle);
        costLabel.setY(getHeight() * 0.20f);
        costLabel.setX(getWidth() / 2 - costLabel.getWidth() / 2);
        addActor(costLabel);

        if(ForestGameScreen.isDebug)
            debugAll();
    }

    public void setStack(ItemStack stack) {
        this.stackToUpgrade = stack;

    }

    private int getUpgradeCost() {
        return 1;
    }
}
