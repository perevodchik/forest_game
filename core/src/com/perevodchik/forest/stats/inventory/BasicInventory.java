package com.perevodchik.forest.stats.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;
import com.perevodchik.forest.entity.Player;
import com.perevodchik.forest.enums.Slot;
import com.perevodchik.forest.items.root.Item;
import com.perevodchik.forest.items.root.ItemStack;
import com.perevodchik.forest.registry.RegistryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicInventory implements IBasicInventory {
    private ArrayList<ItemStack> items;

    public BasicInventory() {
        this.items = new ArrayList<>();
    }

    @Override
    public void set(ItemStack stack, int slot) {
        for(ItemStack stack1: items) {
            Gdx.app.log("check itemId", "[" + stack1.getId().toString() + "] => [" + stack.getId().toString() + "]");
            if(stack.getId().equals(stack1.getId()))
                return;
        }
        items.set(slot, stack);
    }

    @Override
    public void addItem(ItemStack stack) {
        for(ItemStack stack1: items) {
            Gdx.app.log("check itemId", "[" + stack1.getId().toString() + "] => [" + stack.getId().toString() + "]");
            if(stack.getId().equals(stack1.getId()))
                return;
        }
        stack.setEquip(false);
        if (stack.item().isCanStack() && items.size() > 0) {
             for (ItemStack itemStack : items) {
                 if (itemStack.item().getId() != stack.item().getId()) continue;
                 int canBeAdd = itemStack.canAdd();
                 if(stack.getCount() > canBeAdd) {
                     itemStack.add(canBeAdd);
                     stack.remove(canBeAdd);
                 } else {
                     itemStack.add(stack.getCount());
                     return;
                 }
             }
            if(stack.getCount() > 0) {
                items.add(stack);
            }
        } else {
            items.add(stack);
        }
        Player.getPlayer().recalculateInventoryWeight();
    }

    @Override
    public void remove(int index) {
        items.remove(index);
        Player.getPlayer().recalculateInventoryWeight();
    }

    @Override
    public void remove(UUID id) {
        int index = 0;
        for(ItemStack itemStack : items) {
            if(itemStack.getId().equals(id)) {
                items.remove(index);
                Player.getPlayer().recalculateInventoryWeight();
                return;
            }
            index++;
        }
    }

    @Override
    public void remove(ItemStack stack) {
        Gdx.app.error("remove", stack.toString());
        if(!stack.item().isCanStack() || stack.item().getMaxStack() == 1) {
            remove(stack.getId());
            Gdx.app.error("remove", "remove stack by id");
        } else {
            for(ItemStack stack1: items) {
                if(stack1.item() == stack.item()) {
                    if (stack1.getCount() > stack.getCount()) {
                        Gdx.app.error("remove", "if (stack1.getCount() > stack.getCount()) {");
                        stack1.remove(stack.getCount());
                        stack.remove(stack.getCount());
                        return;
                    } else if (stack1.getCount() == stack.getCount()) {
                        Gdx.app.error("remove", "else if (stack1.getCount() == stack.getCount())");
                        items.remove(stack1);
                        stack.remove(stack.getCount());
                        return;
                    } else {
                        Gdx.app.error("remove", "else");
                        int c = stack1.getCount();
                        stack1.remove(c);
                        stack.remove(c);
                    }
                }
            }
        }
//        int index = 0;
//        for(ItemStack itemStack : items) {
//            if(itemStack.getId().equals(stack.getId())) {
//                items.remove(index);
//                Player.getPlayer().recalculateInventoryWeight();
//                return;
//            }
//            index++;
//        }
    }

    @Override
    public int count(ItemStack item) {
        int c = 0;
        for(ItemStack stack: items) {
            if(stack.item() == item.item()) {
                c += stack.getCount();
            }
        }
        return c;
    }

    @Override
    public void replace(ItemStack oldStack, ItemStack newStack) {
        for(ItemStack stack1: items) {
            Gdx.app.log("check itemId", "[" + stack1.getId().toString() + "] => [" + newStack.getId().toString() + "]");
            if(newStack.getId().equals(stack1.getId()))
                return;
        }
        for(int c = 0; c < items.size(); c++) {
            if(items.get(c).getId().equals(oldStack.getId())) {
                items.set(c, newStack);
                items.get(c).setEquip(false);
                Gdx.app.error("replace", "true");
                return;
            }
        }
        Gdx.app.error("replace", "false");
    }

    @Override
    public void dropInventory() {
        for (int c = 0; c < items.size(); c++) {
            ItemStack stack = items.get(c);
            if (stack.item().isCanDrop())
                stack.setEmpty();
        }
        Player.getPlayer().recalculateInventoryWeight();
    }

    @Override
    public void show() {
        for (ItemStack stack : items) {
            System.out.println(stack.toString());
        }
    }

    @Override
    public float getWeight() {
        float weight = 0f;
        for(ItemStack stack : items) {
            if(stack.item() == RegistryManager.empty) continue;
            weight += stack.getCount() * stack.item().getWeight();
        }
        return weight;
    }

    @Override
    public int getItemsCount() {
        int count = 0;
        for(ItemStack stack : items)
            if(stack.item() != RegistryManager.empty)
                count++;
        return count;
    }

    @Override
    public ItemStack get(int index) {
        if(index < 0 || index >= items.size())
            return ItemStack.empty();
        return items.get(index);
    }

    @Override
    public List<ItemStack> get() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder _items = new StringBuilder();
        for(int c = 0; c < items.size(); c++) {
            ItemStack item = items.get(c);
            _items.append("[").append(c).append("]").append("[").append(item.toString()).append("]").append("\n");
        }
        return "BasicInventory{" +
                "items=" + _items +
                '}';
    }
}
