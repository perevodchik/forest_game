package com.perevodchik.forest.utils;

import com.perevodchik.forest.items.root.ItemStack;

public class DropItemData {
    public final ItemStack stack;
    public final MinMax countData;
    public final MinMax chanceData;

    public DropItemData(ItemStack stack, MinMax countData, MinMax chanceData) {
        this.stack = stack;
        this.countData = countData;
        this.chanceData = chanceData;
    }
}
