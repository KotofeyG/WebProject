package com.kotov.restaurant.model.entity;

public class Address extends AbstractEntity {
    private String city;
    private String street;
    private int building;
    private String block;
    private int flat;
    private int entrance;
    private int floor;
    private int intercomCode;

    public Address() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getIntercomCode() {
        return intercomCode;
    }

    public void setIntercomCode(int intercomCode) {
        this.intercomCode = intercomCode;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        Address other = (Address) otherObject;
        if (city != null ? !city.equals(other.city) : other.city != null) {
            return false;
        }
        if (street != null ? !street.equals(other.street) : other.street != null) {
            return false;
        }
        if (building != other.building) {
            return false;
        }
        if (block != null ? !block.equals(other.block) : other.block != null) {
            return false;
        }
        if (flat != other.flat) {
            return false;
        }
        if (entrance != other.entrance) {
            return false;
        }
        if (floor != other.floor) {
            return false;
        }
        return intercomCode == other.intercomCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (city != null ? city.hashCode() : 0);
        result = prime * result + (street != null ? street.hashCode() : 0);
        result = prime * result + building;
        result = prime * result + (block != null ? block.hashCode() : 0);
        result = prime * result + flat;
        result = prime * result + entrance;
        result = prime * result + floor;
        result = prime * result + intercomCode;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,city = ").append(city);
        result.append(" ,street = ").append(street);
        result.append(" ,building = ").append(building);
        result.append(" ,block = ").append(block);
        result.append(" ,flat = ").append(flat);
        result.append(" ,entrance = ").append(entrance);
        result.append(" ,floor = ").append(floor);
        result.append(" ,intercomCode = ").append(intercomCode).append(" ]");
        return result.toString();
    }
}