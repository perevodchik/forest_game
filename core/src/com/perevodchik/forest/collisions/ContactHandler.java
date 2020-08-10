package com.perevodchik.forest.collisions;

import com.perevodchik.forest.entity.Entity;

public interface ContactHandler {
    boolean isValid(Entity entity0, Entity entity1);
    void collision();
    void endCollision();
}
