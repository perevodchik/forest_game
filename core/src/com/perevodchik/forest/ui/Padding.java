package com.perevodchik.forest.ui;

import java.util.HashMap;

public class Padding {
    private HashMap<com.perevodchik.forest.enums.Padding, Integer> paddingMap = new HashMap<>();

    public HashMap<com.perevodchik.forest.enums.Padding, Integer> getPaddingMap() {
        return paddingMap;
    }

    public int getPadding(com.perevodchik.forest.enums.Padding padding) {
        if(paddingMap.containsKey(padding))
            return paddingMap.get(padding);
        return 0;
    }

    public void setPadding(com.perevodchik.forest.enums.Padding padding, int value) {
        paddingMap.put(padding, value);
    }
}
