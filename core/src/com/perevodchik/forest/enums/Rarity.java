package com.perevodchik.forest.enums;

        import com.badlogic.gdx.graphics.Color;

public enum Rarity {
    COMMON(Color.GRAY, 1, 1),
    RARE(Color.GREEN, 2, 1.5),
    LEGENDARY(Color.FIREBRICK, 3, 3),
    IMMORTAL(Color.GOLD, 4, 5);

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
}
