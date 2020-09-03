package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.stats.AttributeValue;
import com.perevodchik.forest.storage.Storage;
import com.perevodchik.forest.ui.Slot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.utils.FontUtil;

import java.util.Map;

public class DescriptionContainer extends Group {
    private BitmapFont listFont;
    private BitmapFont titleFont;
    private BitmapFont buttonsFont;
    private TextButton equipButton;
    private TextButton unEquipButton;
    private TextButton destroyButton;
    private Player player;
    private Window window;
    private Slot slot;
    private Label title;
    private ScrollPane scrollPane;
    private List<String> attributeList;

    public DescriptionContainer(Window window, int x, int y, int width, int height) {
        this.window = window;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        Gdx.app.error("x", String.valueOf(x));
        Gdx.app.error("ForestGameScreen.width", String.valueOf(ForestGameScreen.width));
        Gdx.app.error("y", String.valueOf(y));
        Gdx.app.error("width", String.valueOf(width));
        Gdx.app.error("height", String.valueOf(height));

        this.listFont = FontUtil.generate(30);
        this.titleFont = FontUtil.generate(65);
        this.buttonsFont = FontUtil.generate(50);
        this.player = Player.getPlayer();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = buttonsFont;
        textButtonStyle.downFontColor = Color.CYAN;
        equipButton = new TextButton("Equip", textButtonStyle);
        equipButton.setX(50);
        equipButton.setY(25);
        equipButton.addListener(new EquipItemListener());
        addActor(equipButton);

        unEquipButton = new TextButton("Down", textButtonStyle);
        unEquipButton.setX(50);
        unEquipButton.setY(25);
        unEquipButton.addListener(new DownItemListener());
        addActor(unEquipButton);

        destroyButton = new TextButton("Destroy", textButtonStyle);
        destroyButton.setX(width - 50 - destroyButton.getWidth());
        destroyButton.setY(25);
        destroyButton.addListener(new DestroyItemListener());
        addActor(destroyButton);

        slot = new Slot(((InventoryWindow) window).getSelectedStack(),
                FontUtil.generate(65),
                50,
                height - ForestGameScreen.blockXS - 50,
                ForestGameScreen.blockXS,
                ForestGameScreen.blockXS);
        addActor(slot);

        title = new Label(
                "",
                new Label.LabelStyle(titleFont, Color.WHITE)
        );
        title.setX(slot.getX() + slot.getWidth() + 50);
        title.setY(height - ForestGameScreen.blockXS + 50);
        addActor(title);

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = listFont;
        listStyle.selection = new SpriteDrawable(new Sprite(new Texture("empty.png")));
        attributeList = new List<>(listStyle);
        attributeList.setTypeToSelect(false);
        String[] items = new String[0];
        attributeList.setLayoutEnabled(false);
        attributeList.setItems(items);

        scrollPane = new ScrollPane(attributeList);
        scrollPane.setBounds(
                slot.getX(),
                destroyButton.getY() + destroyButton.getHeight() + 50,
                destroyButton.getX(),
                height * 0.5f);
        scrollPane.setSmoothScrolling(false);
        if(ForestGameScreen.isDebug)
            debugAll();
        scrollPane.setTransform(true);
        scrollPane.setScale(1.15f);
        addActor(scrollPane);

        if(ForestGameScreen.isDebug)
            debugAll();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        equipButton.setVisible(!((InventoryWindow) window).getSelectedStack().isEquip() && ((InventoryWindow) window).getSelectedStack().item().isCanEquip());
        unEquipButton.setVisible(((InventoryWindow) window).getSelectedStack().isEquip() && ((InventoryWindow) window).getSelectedStack().item().isCanEquip());

        super.draw(batch, parentAlpha);
    }

    public void setItemStack(ItemStack stack) {
        title.setText(slot.getStack().item().getDisplayName());
        title.getStyle().fontColor = slot.getStack().getRarity().getColor();

        String[] items = new String[stack.item().getAttributes().size()];
        int counter = 0;
        for(Map.Entry<Attribute, java.util.List<AttributeValue>> e: stack.item().getAttributes().entrySet()) {
            String attribute = e.getKey().toString();
            int value = 0;
            for(AttributeValue v: e.getValue()) {
                value += v.value;
            }
            value *= stack.getRarity().getMultiplier();
            String showAttribute = attribute.concat(" ").concat(String.valueOf(value));
            items[counter++] = showAttribute;
        }

        attributeList.setItems(items);
    }

    private class EquipItemListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            player.equip(((InventoryWindow) window).getSelectedStack());
            ((InventoryWindow) window).selectStack(ItemStack.empty());
            Storage.storage().savePlayerState(player);
            ((InventoryWindow) window).displayItems();
            return true;
        }
    }

    private class DownItemListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            player.downItem(((InventoryWindow) window).getSelectedStack());
            ((InventoryWindow) window).selectStack(ItemStack.empty());
            Storage.storage().savePlayerState(player);
            ((InventoryWindow) window).displayItems();
            return true;
        }
    }

    private class DestroyItemListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            ItemStack stack = ((InventoryWindow) window).getSelectedStack();
            player.decreaseAttributes(((InventoryWindow) window).getSelectedStack());
            stack.setEmpty();
            if(stack.isEquip()) {
                player.getEquipmentInventory().get(((InventoryWindow) window).getSelectedStack().item().getSlot()).set(ItemStack.empty());
            } else {
                player.getBasicInventory().remove(((InventoryWindow) window).getSelectedStack());
            }
            ((InventoryWindow) window).selectStack(ItemStack.empty());
            Storage.storage().savePlayerState(player);
            ((InventoryWindow) window).displayItems();
            return true;
        }
    }

}
