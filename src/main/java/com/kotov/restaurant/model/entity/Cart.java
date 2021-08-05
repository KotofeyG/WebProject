package com.kotov.restaurant.model.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart extends AbstractEntity {
    private Map<Meal, Integer> meals;

    public Cart() {
        meals = new LinkedHashMap<>();
    }

    public Map<Meal, Integer> getMeals() {
        return meals;
    }

    public void setMeals(Map<Meal, Integer> meals) {
        this.meals = meals;
    }

    public Integer put(Meal key, Integer value) {
        return meals.put(key, value);
    }

    public Integer get(Object key) {
        return meals.get(key);
    }

    public boolean containsKey(Object key) {
        return meals.containsKey(key);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        Cart other = (Cart) otherObject;
        return meals != null ? meals.equals(other.meals) : other.meals == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (meals != null ? meals.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,meals = \n");
        for (Meal key : meals.keySet()) {
            result.append(key).append("=").append(meals.get(key)).append(",\n");
        }
        result.delete(result.length() - 2, result.length()).append(" ]");
        return result.toString();
    }
}