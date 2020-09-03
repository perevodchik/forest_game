package com.perevodchik.forest.ui.blacksmith;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.enums.Rarity;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.ui.Padding;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.utils.FontUtil;

public class BlacksmithContainer extends Window {
    private Slot itemSlot;
    private Label itemLabel;
    private Label rareLabel;
    private Label costLabel;
    private Button upgradeButton;
    private ItemStack stackToUpgrade;
    private Label.LabelStyle costLabelStyle;
    Label.LabelStyle itemLabelStyle;

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
        stackToUpgrade = ItemStack.empty();
        itemSlot = new Slot(
                stackToUpgrade,
                FontUtil.generate(),
                (int) (getWidth() / 2 - ForestGameScreen.blockXS / 2), (int) (getHeight() - 50 - ForestGameScreen.blockXS),
                ForestGameScreen.blockXS, ForestGameScreen.blockXS
        );
        addActor(itemSlot);

        itemLabelStyle = new Label.LabelStyle();
        itemLabelStyle.font = FontUtil.generate(55);
        itemLabelStyle.fontColor = Rarity.LEGENDARY.getColor();
        itemLabel = new Label("", itemLabelStyle);
        itemLabel.setY(itemSlot.getY() - 50 - itemLabel.getHeight());
        itemLabel.setX(getWidth() / 2 - itemLabel.getWidth() / 2);
        addActor(itemLabel);

        costLabelStyle = new Label.LabelStyle();
        costLabelStyle.font = FontUtil.generate(40);
        costLabelStyle.fontColor = Color.GOLD;
        costLabel = new Label("Upgrade cost " + getUpgradeCost() + " Gold", costLabelStyle);
        costLabel.setY(getHeight() * 0.20f);
        costLabel.setX(getWidth() / 2 - costLabel.getWidth() / 2);
        addActor(costLabel);

        TextButton.TextButtonStyle upgradeButtonStyle = new TextButton.TextButtonStyle();
        upgradeButtonStyle.font = FontUtil.generate(50);
        upgradeButton = new TextButton("Upgrade!", upgradeButtonStyle);
        upgradeButton.addListener(new BlacksmithWindow.UpgradeListener(this));
        upgradeButton.setX(getWidth() / 2 - upgradeButton.getWidth() / 2);
        upgradeButton.setY(20);
        addActor(upgradeButton);

        if(ForestGameScreen.isDebug)
            debugAll();
    }

    public void setStack(ItemStack stack) {
        this.stackToUpgrade = stack;
        itemSlot.setStack(stack);

        itemLabel.remove();
        itemLabel = new Label(stack.item().getDisplayName(), itemLabelStyle);
        itemLabel.getStyle().fontColor = stack.getRarity().getColor();
        itemLabel.setX(getWidth() / 2 - itemLabel.getWidth() / 2);
        itemLabel.setY(itemSlot.getY() - 50 - itemLabel.getHeight());
        itemLabel.layout();
        addActor(itemLabel);

        costLabel.remove();
        costLabel = new Label("Upgrade cost " + getUpgradeCost() + " Gold", costLabelStyle);
        costLabel.setX(getWidth() / 2 - costLabel.getWidth() / 2);
        costLabel.setY(getHeight() * 0.20f);
        costLabel.layout();
        addActor(costLabel);
    }

    public ItemStack getStackToUpgrade() {
        return stackToUpgrade;
    }

    public int getUpgradeCost() {
        return (int) (stackToUpgrade.getRarity().getMultiplier() * 35);
    }
}
