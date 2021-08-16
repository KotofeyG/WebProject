package com.kotov.restaurant.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Meal extends AbstractEntity {
    private String title;
    private String image;
    private Type type;
    private BigDecimal price;
    private String recipe;
    private LocalDateTime created;
    private boolean isActive;

    public enum Type {
        ROLL("роллы"), NIGIRI("суши"), SASHIMI("сашими"), SOUP("супы")
        , MAIN_DISH("вторые блюда"), SALAD("салаты"), APPETIZER("закуски");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public Meal() {
    }

    public Meal(String title, Type type, BigDecimal price, String recipe, LocalDateTime created) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.recipe = recipe;
        this.created = created;
        this.isActive = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        Meal other = (Meal) otherObject;
        if (title != null ? !title.equals(other.title) : other.title != null) {
            return false;
        }
//        if (image != null ? !image.equals(other.image) : other.image != null) {
//            return false;
//        }
        if (type != null ? !type.equals(other.type) : other.type != null) {
            return false;
        }
        if (price != null ? !price.equals(other.price) : other.price != null) {
            return false;
        }
        if (recipe != null ? !recipe.equals(other.recipe) : other.recipe != null) {
            return false;
        }
        if (created != null ? !created.equals(other.created) : other.created != null) {
            return false;
        }
        return isActive == other.isActive;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (title != null ? title.hashCode() : 0);
//        result = prime * result + (image != null ? image.hashCode() : 0);
        result = prime * result + (type != null ? type.hashCode() : 0);
        result = prime * result + (price != null ? price.hashCode() : 0);
        result = prime * result + (recipe != null ? recipe.hashCode() : 0);
        result = prime * result + (created != null ? created.hashCode() : 0);
        result = prime * result + Boolean.hashCode(isActive);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,title = ").append(title);
//        result.append(" ,image = ").append(image);
        result.append(" ,type = ").append(type);
        result.append(" ,price = ").append(price);
        result.append(" ,recipe = ").append(recipe);
        result.append(" ,created = ").append(created);
        result.append(" ,isActive = ").append(isActive).append(" ]");
        return result.toString();
    }
}