package com.perevodchik.forest.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.enums.Rarity;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;
import com.perevodchik.forest.stats.inventory.BasicInventory;
import com.perevodchik.forest.stats.inventory.EquipmentInventory;
import com.perevodchik.forest.stats.inventory.IBasicInventory;
import com.perevodchik.forest.stats.inventory.IEquipmentInventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Storage {
    static final String DATABASE_NAME = "forest.db";
    static final int DATABASE_VERSION = 2;
    static final String TABLE_INVENTORY_BASIC = "inventory_basic";
    static final String TABLE_INVENTORY_EQUIPMENT = "inventory_equipment";
    static final String PLAYER_TABLE = "player";
    static final String COLUMN_ID = "id";
    static final String COLUMN_ITEM_ID = "item_id";
    static final String COLUMN_ITEM_UUID = "item_uuid";
    static final String COLUMN_ITEM_GRADE = "item_grade";
    static final String COLUMN_ITEM_STACK_LEVEL = "item_stack_level";
    static final String COLUMN_ITEM_STACK_COUNT = "item_stack_count";
    static final String COLUMN_ITEM_SLOT = "item_slot";
    private static Storage storage = null;
    private Database db;

    public void saveBasicInventory(IBasicInventory inventory) {
        clearBasicInventory();
        List<ItemStack> items = inventory.get();
        for(int c = 0; c < items.size(); c++) {
            saveItemToBasicInventory(items.get(c));
        }
    }

    public void saveEquipmentInventory(IEquipmentInventory inventory) {
        clearEquipmentInventory();
        Map<Slot, ItemStack> items = inventory.getInventory();
        for(Map.Entry<Slot, ItemStack> e: items.entrySet()) {
            saveItemToEquipmentInventory(e.getValue());
        }
    }

    public void clearBasicInventory() {
        final StringBuilder clearBasicInventoryQuery = new StringBuilder()
                .append("DELETE FROM ").append(TABLE_INVENTORY_BASIC).append(";");
//        Gdx.app.log(TABLE_INVENTORY_BASIC, clearBasicInventoryQuery.toString());
        try {
            db.execSQL(clearBasicInventoryQuery.toString());
        } catch (SQLiteGdxException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void clearEquipmentInventory() {
        final StringBuilder clearEquipmentInventoryQuery = new StringBuilder()
                .append("DELETE FROM ").append(TABLE_INVENTORY_EQUIPMENT).append(";");
//        Gdx.app.log(TABLE_INVENTORY_EQUIPMENT, clearEquipmentInventoryQuery.toString());
        try {
            db.execSQL(clearEquipmentInventoryQuery.toString());
        } catch (SQLiteGdxException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void saveItemToBasicInventory(ItemStack stack) {
        if(stack.item() == RegistryManager.empty)
            return;
        final StringBuilder saveItemQuery = new StringBuilder()
                .append("INSERT INTO ").append(TABLE_INVENTORY_BASIC).append(" (")
                .append(COLUMN_ITEM_ID).append(", ")
                .append(COLUMN_ITEM_UUID).append(", ")
                .append(COLUMN_ITEM_GRADE).append(", ")
                .append(COLUMN_ITEM_STACK_COUNT).append(")")
                .append(" VALUES (")
                .append(stack.item().getId()).append(", '")
                .append(stack.getId().toString()).append("', ")
                .append(stack.getRarity().getGrade()).append(", ")
                .append(stack.getCount())
                .append(");");
        Gdx.app.log(TABLE_INVENTORY_BASIC, saveItemQuery.toString());
        try {
            db.execSQL(saveItemQuery.toString());
        } catch (SQLiteGdxException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void saveItemToEquipmentInventory(ItemStack stack) {
        final StringBuilder saveItemQuery = new StringBuilder()
                .append("INSERT INTO ").append(TABLE_INVENTORY_EQUIPMENT).append(" (")
                .append(COLUMN_ITEM_ID).append(", ")
                .append(COLUMN_ITEM_UUID).append(", ")
                .append(COLUMN_ITEM_GRADE).append(", ")
                .append(COLUMN_ITEM_STACK_COUNT).append(")")
                .append(" VALUES (")
                .append(stack.item().getId()).append(", '")
                .append(stack.getId().toString()).append("', ")
                .append(stack.getRarity().getGrade()).append(", ")
                .append(stack.getCount())
                .append(");");
        Gdx.app.log(TABLE_INVENTORY_EQUIPMENT, saveItemQuery.toString());
        try {
            db.execSQL(saveItemQuery.toString());
        } catch (SQLiteGdxException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void savePlayerState(Player player) {
        saveBasicInventory(player.getBasicInventory());
        saveEquipmentInventory(player.getEquipmentInventory());
    }

    public void loadPlayerState(Player player) {
        loadBasicInventory(player);
        loadEquipmentInventory(player);
        player.recalculateStats();
        Gdx.app.error(TABLE_INVENTORY_BASIC, player.getBasicInventory().toString());
        Gdx.app.error(TABLE_INVENTORY_EQUIPMENT, player.getEquipmentInventory().toString());
    }

    public void loadBasicInventory(Player player) {
        IBasicInventory basicInventory = new BasicInventory();
        player.setBasicInventory(basicInventory);
        StringBuilder loadBasicInventoryQuery = new StringBuilder()
                .append("SELECT ")
                .append(COLUMN_ITEM_ID).append(", ")
                .append(COLUMN_ITEM_GRADE).append(", ")
                .append(COLUMN_ITEM_STACK_COUNT).append(", ")
                .append(COLUMN_ITEM_UUID).append(" FROM ")
                .append(TABLE_INVENTORY_BASIC).append(";");
        try {
            DatabaseCursor c = db.rawQuery(loadBasicInventoryQuery.toString());
            if(c == null) return;
            while(c.next()) {
                int itemId = c.getInt(0);
                int itemRarity = c.getInt(1);
                int itemStackCount = c.getInt(2);
                UUID itemStackUuid = UUID.fromString(c.getString(3));
                Rarity rarity = Rarity.getRarityByGrade(itemRarity);
                if(rarity == null)
                    rarity = Rarity.COMMON;
                ItemStack stack1 = new ItemStack(RegistryManager.getInstance().getItemById(itemId), itemStackCount, itemStackUuid);
                stack1.setRarity(rarity);
                if(stack1.item() != RegistryManager.empty)
                    basicInventory.addItem(stack1);
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void loadEquipmentInventory(Player player) {
        IEquipmentInventory equipmentInventory = new EquipmentInventory(player);
        player.setEquipmentInventory(equipmentInventory);

        StringBuilder loadBasicInventoryQuery = new StringBuilder()
                .append("SELECT ")
                .append(COLUMN_ITEM_ID).append(", ")
                .append(COLUMN_ITEM_GRADE).append(", ")
                .append(COLUMN_ITEM_STACK_COUNT).append(", ")
                .append(COLUMN_ITEM_UUID).append(" FROM ")
                .append(TABLE_INVENTORY_EQUIPMENT).append(";");
        try {
            DatabaseCursor c = db.rawQuery(loadBasicInventoryQuery.toString());
            if(c == null) return;
            while(c.next()) {
                int itemId = c.getInt(0);
                int itemRarity = c.getInt(1);
                int itemStackCount = c.getInt(2);
                UUID itemStackUuid = UUID.fromString(c.getString(3));
                Rarity rarity = Rarity.getRarityByGrade(itemRarity);
                if(rarity == null)
                    rarity = Rarity.COMMON;
                ItemStack stack1 = new ItemStack(RegistryManager.getInstance().getItemById(itemId), itemStackCount, itemStackUuid);
                stack1.setRarity(rarity);
                if(stack1.item() != RegistryManager.empty)
                    player.equip(stack1);
            }
            Slot[] slots = new Slot[] {
                    Slot.BODY,
                    Slot.HEAD,
                    Slot.MAIN_HAND,
                    Slot.SECOND_HAND,
                    Slot.LEFT_GLOVE,
                    Slot.RIGHT_GLOVE,
                    Slot.TROUSERS,
                    Slot.SHOE
            };
            for(Slot slot: slots) {
                ItemStack equippedStack = equipmentInventory.get(slot);
                if(equippedStack == null) {
                    equippedStack = new ItemStack(RegistryManager.empty);
                    equipmentInventory.equip(equippedStack);
                }
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public static Storage storage() {
        if(storage == null)
            storage = new Storage();
        return storage;
    }

    public Storage() {
        init();
    }

   private void init() {
        try {
            final StringBuilder createBasicInventoryTable = new StringBuilder()
                    .append("CREATE TABLE IF NOT EXISTS ").append(TABLE_INVENTORY_BASIC).append(" (")
                    .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_ITEM_ID).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_ITEM_GRADE).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_ITEM_STACK_COUNT).append(" INTEGER NOT NULL DEFAULT 1, ")
                    .append(COLUMN_ITEM_SLOT).append(" INTEGER NOT NULL DEFAULT 2, ")
                    .append(COLUMN_ITEM_UUID).append(" TEXT NOT NULL);");
            final StringBuilder createEquipmentInventoryTable = new StringBuilder()
                    .append("CREATE TABLE IF NOT EXISTS ").append(TABLE_INVENTORY_EQUIPMENT).append(" (")
                    .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(COLUMN_ITEM_ID).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_ITEM_GRADE).append(" INTEGER NOT NULL, ")
                    .append(COLUMN_ITEM_STACK_COUNT).append(" INTEGER NOT NULL DEFAULT 1, ")
                    .append(COLUMN_ITEM_SLOT).append(" TEXT DEFAULT 'MAIN_HAND', ")
                    .append(COLUMN_ITEM_UUID).append(" TEXT NOT NULL);");
            db = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, createBasicInventoryTable.toString(), null);

            db.setupDatabase();
            db.openOrCreateDatabase();

            db.execSQL(createEquipmentInventoryTable.toString());
        } catch (SQLiteGdxException sqlE) {
            sqlE.printStackTrace();
        }
    }

    private static class Turtle<Q, W> {
        Q first;
        W second;

        public Turtle(Q theQ, W theW) {
            first = theQ;
            second = theW;
        }
    }
}
