package com.perevodchik.forest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.perevodchik.forest.ForestGameScreen;
import com.perevodchik.forest.enums.Attribute;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;
import com.perevodchik.forest.stats.AttributeValue;
import com.perevodchik.forest.stats.inventory.IBasicInventory;
import com.perevodchik.forest.stats.inventory.IEquipmentInventory;
import com.perevodchik.forest.storage.Storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Player extends EntityLive {
    private static Player player;
    private IBasicInventory basicInventory;
    private IEquipmentInventory equipmentInventory;
    private float maxInventoryWeight = 100f;
    private float inventoryWeight = 0f;
    public static boolean canGoNextStage = false;

    private Player(World world, float width, float height, BodyDef.BodyType type, Texture texture) {
        super(world, width, height, type, texture);
        setSpeed(3.5f);
        attributeMap().put(Attribute.SPEED, Collections.singletonList(new AttributeValue(getSpeed())));
    }

    public void downItem(ItemStack itemToDown) {
        if(itemToDown.isEquip()) {
            itemToDown.setEquip(false);
            decreaseAttributes(itemToDown);
            ItemStack stack0 = ItemStack.empty();
            stack0.set(itemToDown);
            equipmentInventory.get(stack0.item().getSlot()).set(ItemStack.empty().setEquip(true));
            basicInventory.addItem(stack0);
        }
        recalculateStats();
        showAttributes();
    }

    public void equip(ItemStack newEquipItem) {
        if(equipmentInventory.get(newEquipItem.item().getSlot()).item() == RegistryManager.empty) {
            equipmentInventory.equip(newEquipItem);
            basicInventory.remove(newEquipItem.getId());
        } else {
            ItemStack oldItem = ItemStack.from(equipmentInventory.get(newEquipItem.item().getSlot()));
            decreaseAttributes(oldItem);
            basicInventory.remove(newEquipItem);
            basicInventory.addItem(oldItem);
            equipmentInventory.equip(newEquipItem);
        }

        increaseAttributes(newEquipItem);
        recalculateStats();
        showAttributes();
    }

    @Override
    public void setDead() {
        ForestGameScreen.isPlayerDead = true;
        ForestGameScreen.isPause = true;
    }

    private void showAttributes() {
        System.out.println("***************");
        for(Map.Entry<Attribute, List<AttributeValue>> attributeEntry: attributeMap().entrySet()) {
            String a = attributeEntry.getKey().name();
            double v = 0;
            for(AttributeValue value: attributeEntry.getValue()) {
                v += value.value;
            }
            Gdx.app.log(a, String.valueOf(v));
        }
        System.out.println("***************");
    }

    public void increaseAttributes(ItemStack stack) {
        Gdx.app.log("equip", stack.toString());
        for(Map.Entry<Attribute, List<AttributeValue>> attributeEntry: stack.item().getAttributes().entrySet()) {
            List<AttributeValue> attributeValues = attributeEntry.getValue();
            for (AttributeValue value : attributeValues) {
                AttributeValue newValue = new AttributeValue(value.id, (float) (value.value * stack.getRarity().getMultiplier()));
                Gdx.app.error("increase attribute", value.toString() + " => " + newValue.toString());
                increaseAttribute(attributeEntry.getKey(), newValue);
            }
        }
    }

    public void decreaseAttributes(ItemStack stack) {
        for(Map.Entry<Attribute, List<AttributeValue>> attributeEntry: stack.item().getAttributes().entrySet()) {
            Gdx.app.error(attributeEntry.getKey().toString(), attributeEntry.getValue().size() + "");
            List<AttributeValue> attributeValues = attributeEntry.getValue();
            for(AttributeValue value: attributeValues) {
                Gdx.app.error("down attribute", value.toString());
                decreaseAttribute(attributeEntry.getKey(), value);
            }
        }
    }

    public void increaseAttribute(Attribute attribute, AttributeValue value) {
        if(!attributeMap().containsKey(attribute)) {
            List<AttributeValue> values = new ArrayList<>();
            values.add(value);
            attributeMap().put(attribute, values);
        } else {
            attributeMap().get(attribute).add(value);
        }
    }

    public void decreaseAttribute(Attribute attribute, AttributeValue value) {
        if(!attributeMap().containsKey(attribute)) return;
        List<AttributeValue> values = attributeMap().get(attribute);
        for(AttributeValue attributeValue: values) {
            if(attributeValue.id.equals(value.id)) {
                values.remove(attributeValue);
                Gdx.app.log("find attribute", "id " + value.id.toString() + " => " + attribute.name() + " => " + value.value);
                return;
            }
        }
    }

    public IEquipmentInventory getEquipmentInventory() {
        return equipmentInventory;
    }

    public IBasicInventory getBasicInventory() {
        return basicInventory;
    }

    public void pickItem(ItemStack stack) {
        Gdx.app.error("pick", stack.toString());
        basicInventory.addItem(stack);
    }

    public static void createPlayer(World world) {
        player = new Player(world, 10, 10, BodyDef.BodyType.DynamicBody, new Texture("model.png"));
        Storage.storage().loadPlayerState(player);
        player.setCurrentHealth(player.getMaxHealth());
        player.setCurrentEnergy(player.getMaxEnergy());
    }

    public static Player getPlayer() {
        return player;
    }

    public void recalculateInventoryWeight() {
        float basicInventoryWeight = 0;
        float equipmentInventoryWeight = 0;
        if(basicInventory != null)
            basicInventoryWeight = basicInventory.getWeight();
        if(equipmentInventory != null)
            equipmentInventoryWeight = equipmentInventory.getWeight();

        recalculateSpeed(basicInventoryWeight + equipmentInventoryWeight);
    }

    private void recalculateSpeed(float itemsWeight) {
        this.inventoryWeight = itemsWeight;
        float speedMultiplier = inventoryWeight / maxInventoryWeight;
        float speed = attribute(Attribute.SPEED) * 0.5f;
        float newSpeed = speed * (1 - speedMultiplier);
        if(newSpeed < 0) newSpeed = 0;
        if(newSpeed > getMaxSpeed()) newSpeed = getMaxSpeed();
        setSpeed(speed + newSpeed);
    }

    public void setBasicInventory(IBasicInventory basicInventory) {
        this.basicInventory = basicInventory;
    }

    public void setEquipmentInventory(IEquipmentInventory equipmentInventory) {
        this.equipmentInventory = equipmentInventory;
    }
}
