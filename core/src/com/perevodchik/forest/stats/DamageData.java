package com.perevodchik.forest.stats;

import com.perevodchik.forest.entity.Entity;
import com.perevodchik.forest.enums.DamageSource;

public final class DamageData {
    private Entity entity;
    private float value;
    private DamageSource source;

    public DamageData(Entity entity, float value, DamageSource source) {
        this.entity = entity;
        this.value = value;
        this.source = source;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getValue() {
        return value;
    }

    public DamageSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "DamageData{" +
                "entity=" + entity.getClass().getSimpleName() +
                ", value=" + value +
                ", source=" + source +
                '}';
    }
}
