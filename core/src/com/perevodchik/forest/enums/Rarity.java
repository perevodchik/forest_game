package com.perevodchik.forest.enums;

import com.badlogic.gdx.graphics.Color;

public enum Rarity {
    COMMON(Color.GRAY, 1, 1.2),
    RARE(Color.GREEN, 2, 1.5),
    LEGENDARY(Color.FIREBRICK, 3, 5),
    IMMORTAL(Color.GOLD, 4, 10);

    private Color color;
    private int grade;
    private double multiplier;

    Rarity(Color color, int grade, double multiplier) {
        this.color = color;
        this.grade = grade;
        this.multiplier = multiplier;
    }

    public Color getColor() {
        return color;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getGrade() {
        return grade;
    }

    public static Rarity getRarityByGrade(int i) {
        for(Rarity r: Rarity.values()) {
            if(r.grade == i)
                return r;
        }
        return null;
    }

    public static int getMaxGrade() {
        int maxGrade = 0;
        for(Rarity r: Rarity.values()) {
            if(r.getGrade() > maxGrade)
                maxGrade = r.getGrade();
        }
        return maxGrade;
    }
}
