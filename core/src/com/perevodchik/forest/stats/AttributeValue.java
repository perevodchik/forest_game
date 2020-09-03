package com.perevodchik.forest.stats;

import java.util.UUID;

public class AttributeValue {
    public final float value;
    public final UUID id;

    public AttributeValue(float value) {
        this.value = value;
        id = UUID.randomUUID();
    }
    public AttributeValue(UUID id, float value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "value=" + value +
                ", id=" + id +
                '}';
    }
}
