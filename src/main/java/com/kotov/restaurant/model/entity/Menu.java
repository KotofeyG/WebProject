package com.kotov.restaurant.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Menu extends AbstractEntity {
    private long menuId;
    private String title;
    private String type;
    private List<Meal> meals;
    private LocalDate created;
    private LocalDate updated;

    {
        meals = new ArrayList<>();
    }

    public Menu() {
    }

    public Menu(String title, String type) {
        this.title = title;
        this.type = type;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    public Menu(String title, String type, List<Meal> meals) {
        this.title = title;
        this.type = type;
        this.meals = meals;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    public boolean addAll(Collection<? extends Meal> c) {
        return meals.addAll(c);
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Menu other = (Menu) otherObject;
        if (menuId != other.menuId) {
            return false;
        }
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
        int result = (int)(menuId ^ (menuId >>> 32));
        result = prime * result + (title != null ? title.hashCode() : 0);
        result = prime * result + (type != null ? type.hashCode() : 0);
        result = prime * result + (meals != null ? meals.hashCode() : 0);
        result = prime * result + (created != null ? created.hashCode() : 0);
        result = prime * result + (updated != null ? updated.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append("[ menuId = ").append(menuId);
        result.append(" ,title = ").append(title);
        result.append(" ,type = ").append(type);
        result.append(" ,meals = ").append(meals);
        result.append(" ,created = ").append(created);
        result.append(" ,updated = ").append(updated).append(" ]");
        return result.toString();
    }
}