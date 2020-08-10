package com.perevodchik.forest.ui.inventory;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.ui.ItemSlot;
import com.perevodchik.forest.ui.Window;
import com.perevodchik.forest.utils.FontUtil;

public class EquipmentInventoryContainer extends Group {
    private Window window;
    private Player player = Player.getPlayer();
    private BitmapFont font;

    public EquipmentInventoryContainer(Window window) {
        this.window = window;
        font = FontUtil.generate(40);
        int marginX = (int) (ForestGameScreen.width * 0.05), marginY = (int) (ForestGameScreen.height * 0.05);

        {
            SlotContainer equipmentSlotContainer = new SlotContainer(ForestGameScreen.blockXS + marginX,
                    ForestGameScreen.height - marginY - (30 * 2) - (ForestGameScreen.blockXS * 2),
                    ForestGameScreen.blockXS * 5 + (15 * 4),
                    (30 * 2) + (ForestGameScreen.blockXS * 2), false);

            ItemSlot itemSlotBody = new ItemSlot(1, com.perevodchik.forest.enums.Slot.BODY,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.BODY), font,
                    50, 10,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);
            ItemSlot itemSlotHead = new ItemSlot(0, com.perevodchik.forest.enums.Slot.HEAD,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.HEAD), font,
                    50, ForestGameScreen.blockXS + 50,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);

            ItemSlot itemSlotShoe = new ItemSlot(2, com.perevodchik.forest.enums.Slot.SHOE,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.SHOE), font,
                    (int) (equipmentSlotContainer.getWidth() / 2 - ForestGameScreen.blockXS / 2), ForestGameScreen.blockXS + 50,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);
            ItemSlot itemSlotLegs = new ItemSlot(3, com.perevodchik.forest.enums.Slot.TROUSERS,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.TROUSERS), font,
                    (int) (equipmentSlotContainer.getWidth() / 2 - ForestGameScreen.blockXS / 2), 10,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);

            ItemSlot itemSlotMainHand = new ItemSlot(4, com.perevodchik.forest.enums.Slot.MAIN_HAND,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.MAIN_HAND), font,
                    (int) (equipmentSlotContainer.getWidth() - 50 - ForestGameScreen.blockXS), ForestGameScreen.blockXS + 50,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);
            ItemSlot itemSlotSecondHand = new ItemSlot(5, com.perevodchik.forest.enums.Slot.SECOND_HAND,
                    player.getEquipmentInventory().get(com.perevodchik.forest.enums.Slot.SECOND_HAND), font,
                    (int) (equipmentSlotContainer.getWidth() - 50 - ForestGameScreen.blockXS), 10,
                    ForestGameScreen.blockXS, ForestGameScreen.blockXS);

            itemSlotHead.addListener(new InventoryWindow.SlotController(itemSlotHead, window));
            itemSlotBody.addListener(new InventoryWindow.SlotController(itemSlotBody, window));
            itemSlotShoe.addListener(new InventoryWindow.SlotController(itemSlotShoe, window));
            itemSlotLegs.addListener(new InventoryWindow.SlotController(itemSlotLegs, window));
            itemSlotMainHand.addListener(new InventoryWindow.SlotController(itemSlotMainHand, window));
            itemSlotSecondHand.addListener(new InventoryWindow.SlotController(itemSlotSecondHand, window));

            equipmentSlotContainer.addActor(itemSlotHead);
            equipmentSlotContainer.addActor(itemSlotBody);
            equipmentSlotContainer.addActor(itemSlotShoe);
            equipmentSlotContainer.addActor(itemSlotLegs);
            equipmentSlotContainer.addActor(itemSlotMainHand);
            equipmentSlotContainer.addActor(itemSlotSecondHand);
            addActor(equipmentSlotContainer);
        }
        if(ForestGameScreen.isDebug)
            debugAll();
    }

}
