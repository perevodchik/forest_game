package com.perevodchik.forest.entity;

import com.perevodchik.forest.entity.ai.AI;

import java.util.ArrayList;

public interface AIEntity {
    ArrayList<AI> aiList = new ArrayList<>();
    void tickAI();
    void initAI();
}
