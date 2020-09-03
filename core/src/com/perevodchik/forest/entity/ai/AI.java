package com.perevodchik.forest.entity.ai;

public interface AI {
    boolean execute();
    boolean update();
    int getCurrentSteep();
    boolean canContinue();
    AI getNewAI();
}
