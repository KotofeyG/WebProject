package com.kotov.restaurant.model.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order extends AbstractEntity {
    private Map<Meal, Integer> meals;
    private long userId;
    private Address address;
    private LocalTime deliveryTime;
    private boolean isCash;
    private LocalDateTime created;
    private Status status;

    public enum Status {
        IN_PROCESS("В обработке"),
        APPROVED("Одобрен"),
        PAID("Оплачен"),
        REJECTED("Отклонен");

        String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    {
        meals = new LinkedHashMap<>();
    }

    public Order() {
    }

    public Order(Map<Meal, Integer> meals, long userId, LocalTime deliveryTime, boolean isCash) {
        this.meals = meals;
        this.userId = userId;
        this.deliveryTime = deliveryTime;
        this.isCash = isCash;
        this.created = LocalDateTime.now();
        this.status = Status.IN_PROCESS;
    }

    public Order(Map<Meal, Integer> meals, long userId, Address address, LocalTime deliveryTime, boolean isCash) {
        this.meals = meals;
        this.userId = userId;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.isCash = isCash;
        this.created = LocalDateTime.now();
        this.status = Status.IN_PROCESS;
    }

    public Map<Meal, Integer> getMeals() {
        return meals;
    }

    public void setMeals(Map<Meal, Integer> meals) {
        this.meals = meals;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isCash() {
        return isCash;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        Order other = (Order) otherObject;
        if (meals != null ? !meals.equals(other.meals) : other.meals != null) {
            return false;
        }
        if (userId != other.userId) {
            return false;
        }
        if (address != null ? !address.equals(other.address) : other.address != null) {
            return false;
        }
        if (isCash != other.isCash) {
            return false;
        }
        if (deliveryTime != null ? !deliveryTime.equals(other.deliveryTime) : other.deliveryTime != null) {
            return false;
        }
        if (created != null ? !created.equals(other.created) : other.created != null) {
            return false;
        }
        return status == other.status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (meals != null ? meals.hashCode() : 0);
        result = prime * result + Long.hashCode(userId);
        result = prime * result + (address != null ? address.hashCode() : 0);
        result = prime * result + Boolean.hashCode(isCash);
        result = prime * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = prime * result + (created != null ? created.hashCode() : 0);
        result = prime * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,meals = ").append(meals);
        result.append(" ,userId = ").append(userId);
        result.append(" ,address = ").append(address);
        result.append(" ,isCash = ").append(isCash);
        result.append(" ,deliveryTime = ").append(deliveryTime);
        result.append(" ,created = ").append(created);
        result.append(" ,status = ").append(status).append(" ]");
        return result.toString();
    }
}