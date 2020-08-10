package com.perevodchik.forest.enums;

public enum AnimateState {
    STAY(0),
    WALK(1),
    ATTACK(2),
    SPELL(3),
    DEATH(4);

    private int value;

    AnimateState(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
