package com.perevodchik.forest.registry;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.items.root.Item;

import java.util.Map;

public interface IRegistry {
    Map<Class<? extends Item>, Item> getItemDictionary();
    Map<Class<? extends Entity>, Entity> getEntityDictionary();

    void registerItem(Class<? extends Item> itemClass, Item item) throws Exception;
    Item getItemById(int id);
    Item getItemByName(String name);

    void registerEntity(Class<? extends Entity> entityClass, Entity entity);
}
