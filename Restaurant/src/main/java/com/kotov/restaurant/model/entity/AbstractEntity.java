package com.kotov.restaurant.model.entity;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {
    private long id;

    public AbstractEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) otherObject;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append("[ id = ").append(id);
        return result.toString();
    }
}