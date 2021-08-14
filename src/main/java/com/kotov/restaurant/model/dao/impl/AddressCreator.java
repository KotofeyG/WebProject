package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.model.entity.Address;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.kotov.restaurant.model.dao.ColumnName.*;

public class AddressCreator {
    private AddressCreator() {
    }

    public static Address create(ResultSet resultSet) throws SQLException {
        Address address = new Address();
        address.setId(resultSet.getLong(ADDRESS_ID));
        address.setCity(Address.City.valueOf(resultSet.getString(CITY).toUpperCase()));
        address.setStreet(resultSet.getString(STREET));
        address.setBuilding(resultSet.getInt(BUILDING));
        address.setBlock(resultSet.getString(BLOCK));
        address.setFlat(resultSet.getInt(FLAT));
        address.setEntrance(resultSet.getInt(ENTRANCE));
        address.setFloor(resultSet.getInt(FLOOR));
        address.setIntercomCode(resultSet.getString(INTERCOM_CODE));
        return address;
    }
}