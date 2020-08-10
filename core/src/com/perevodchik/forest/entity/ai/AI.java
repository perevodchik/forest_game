package com.perevodchik.forest.entity.ai;

public interface AI {
    boolean execute();
    boolean steep();
    int getCurrentSteep();
    boolean canContinue();
    AI getNewAI();
}
