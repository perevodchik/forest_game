package com.perevodchik.forest.stats;

import java.util.UUID;

public class AttributeValue {
    public final float value;
    public final UUID id = UUID.randomUUID();

    public AttributeValue(float value) {
        this.value = value;
    }
}
