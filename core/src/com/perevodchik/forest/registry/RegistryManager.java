package com.perevodchik.forest.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.ItemEmpty;
import com.perevodchik.forest.items.ItemIronArmor;
import com.perevodchik.forest.items.ItemIronKnife;
import com.perevodchik.forest.items.ItemMagickList;
import com.perevodchik.forest.items.ItemMoonArmor;
import com.perevodchik.forest.items.ItemWoodBow;
import com.perevodchik.forest.items.root.Item;
import com.perevodchik.forest.items.ItemGoldenCoin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RegistryManager implements IRegistry {
    private static IRegistry manager = null;
    private static final Map<Class<? extends Item>, Item> itemDictionary = new HashMap<>();
    private static final Map<Class<? extends Entity>, Entity> entityDictionary = new HashMap<>();
    public static int idIndex = 0;

    public static Item empty = new ItemEmpty(1, idIndex++, 0, 0, 0f, "empty", "", Slot.MAIN_HAND, null, false, true, false, true);
    public static Item goldenCoin = new ItemGoldenCoin(1000, idIndex++, 0, 0, 0.01f,  "golden_coin", "Golden Coin", Slot.MAIN_HAND, new Texture("golden_coin.png"), false, true, false, true);
    public static Item ironArmor = new ItemIronArmor(1, idIndex++, 100, 1, 10f, "iron_armor", "Iron Armor", Slot.BODY, new Texture("armor.png"), true, false, true, true);
    public static Item ironKnife = new ItemIronKnife(1, idIndex++, 100, 1, 10f, "iron_knife", "Iron Knife", Slot.MAIN_HAND, new Texture("knife0.png"), true, false, true, true);
    public static Item magicList = new ItemMagickList(1, idIndex++, 100, 1, 1f, "magic_list", "Magic List", Slot.MAIN_HAND, new Texture("order.png"), true, false, false, false);
    public static Item woodBow = new ItemWoodBow(1, idIndex++, 100, 1, 3f, "wood_bow", "Wood Bow", Slot.MAIN_HAND, new Texture("wood_bow.png"), true, false, false, false);
    public static Item moonArmor = new ItemMoonArmor(1, idIndex++, 100, 1, 10f, "moon_armor", "Moon Armor", Slot.BODY, new Texture("moon_armor.png"), true, false, true, true);

    private RegistryManager() {
        manager = this;
        registerItems();
    }

    @Override
    public Map<Class<? extends Item>, Item> getItemDictionary() {
        return itemDictionary;
    }

    @Override
    public Map<Class<? extends Entity>, Entity> getEntityDictionary() {
        return entityDictionary;
    }

    @Override
    public void registerItem(Class<? extends Item> itemClass, Item item) {
        if (itemDictionary.containsKey(itemClass)) {
            Gdx.app.error("registerItem", "item " + itemClass.getName() + " already register");
            return;
        }
        itemDictionary.put(itemClass, item);
    }

    @Override
    public void registerEntity(Class<? extends Entity> entityClass, Entity entity) {
        if (entityDictionary.containsKey(entityClass)) {
            Gdx.app.error("registerEntity", "entity " + entityClass.getCanonicalName() + " already register");
            return;
        }
        entityDictionary.put(entityClass, entity);
    }

    @Override
    public Item getItemById(int id) {
        for (Item item : itemDictionary.values()) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    @Override
    public Item getItemByName(String name) {
        for (Item item : itemDictionary.values()) if (item.getRegistryName().toLowerCase().equals(name.toLowerCase())) return item;
        return null;
    }

    public static IRegistry getInstance() {
        if (manager == null) manager = new RegistryManager();
        return manager;
    }

    private void registerItems() {
        registerItem(ItemEmpty.class, empty);
        registerItem(ItemGoldenCoin.class, goldenCoin);
        registerItem(ItemIronArmor.class, ironArmor);
        registerItem(ItemIronKnife.class, ironKnife);
        registerItem(ItemMagickList.class, magicList);
        registerItem(ItemWoodBow.class, woodBow);
        registerItem(ItemMoonArmor.class, moonArmor);
    }
}
