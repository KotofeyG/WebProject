package com.kotov.restaurant.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Menu extends AbstractEntity {
    private String title;
    private Type type;
    private List<Meal> meals;
    private LocalDate created;
    private LocalDate updated;

    public enum Type {
        ROLL, NIGIRI, SASHIMI, SOUP, MAIN_DISH, SALAD, APPETIZER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    {
        meals = new ArrayList<>();
    }

    public Menu() {
    }

    public Menu(String title, Type type) {
        this.title = title;
        this.type = type;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    public Menu(Type type, List<Meal> meals) {
        this.type = type;
        this.meals = meals;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    public Menu(String title, Type type, List<Meal> meals) {
        this.title = title;
        this.type = type;
        this.meals = meals;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    public boolean addAll(Collection<? extends Meal> c) {
        return meals.addAll(c);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public int size() {
        return meals.size();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        Menu other = (Menu) otherObject;
        if (title != null ? !title.equals(other.title) : other.title != null) {
            return false;
        }
        if (type != null ? !type.equals(other.type) : other.type != null) {
            return false;
        }
        if (meals != null ? !meals.equals(other.meals) : other.meals != null) {
            return false;
        }
        if (created != null ? !created.equals(other.created) : other.created != null) {
            return false;
        }
        return updated != null ? updated.equals(other.updated) : other.updated == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (title != null ? title.hashCode() : 0);
        result = prime * result + (type != null ? type.hashCode() : 0);
        result = prime * result + (meals != null ? meals.hashCode() : 0);
        result = prime * result + (created != null ? created.hashCode() : 0);
        result = prime * result + (updated != null ? updated.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,title = ").append(title);
        result.append(" ,type = ").append(type);
        result.append(" ,meals = ").append(meals);
        result.append(" ,created = ").append(created);
        result.append(" ,updated = ").append(updated).append(" ]");
        return result.toString();
    }
}