package com.kotov.restaurant.model.entity;

public class Address extends Entity {
    private long addressId;
    private String city;
    private int building;
    private String block;
    private int flat;
    private int entrance;
    private int floor;
    private int intercomCode;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Address other = (Address) otherObject;
        if (addressId != other.addressId) {
            return false;
        }
        if (city != null ? !city.equals(other.city) : other.city != null) {
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
        int result = (int) (addressId ^ (addressId >>> 32));
        result = prime * result + (city != null ? city.hashCode() : 0);
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
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append("[ addressId = ").append(addressId);
        result.append(" ,city = ").append(city);
        result.append(" ,building = ").append(building);
        result.append(" ,block = ").append(block);
        result.append(" ,flat = ").append(flat);
        result.append(" ,entrance = ").append(entrance);
        result.append(" ,floor = ").append(floor);
        result.append(" ,intercomCode = ").append(intercomCode).append(" ]");
        return result.toString();
    }
}