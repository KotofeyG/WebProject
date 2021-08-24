package com.kotov.restaurant.model.dao;

public class ColumnName {
    /* users table */
    public static final String USER_ID = "users.id";
    public static final String LOGIN = "users.login";
    public static final String EMAIL_ADDRESS = "users.email_address";
    public static final String FIRST_NAME = "users.first_name";
    public static final String MIDDLE_NAME = "users.patronymic";
    public static final String LAST_NAME = "users.last_name";
    public static final String MOBILE_NUMBER = "users.mobile_number";
    public static final String REGISTERED = "users.registered";
    /* roles table */
    public static final String USER_ROLE = "roles.role";
    /* user_statuses table */
    public static final String USER_STATUS = "user_statuses.status";
    /* address table */
    public static final String ADDRESS_ID = "address.id";
    public static final String STREET = "address.street";
    public static final String BUILDING = "address.building";
    public static final String BLOCK = "address.block";
    public static final String FLAT = "address.flat";
    public static final String ENTRANCE = "address.entrance";
    public static final String FLOOR = "address.floor";
    public static final String INTERCOM_CODE = "address.intercom_code";
    /* city_names table */
    public static final String CITY = "city_names.city";
    /* menus table */
    public static final String MENU_ID = "menus.id";
    public static final String MENU_TITLE = "menus.title";
    public static final String MENU_CREATED = "menus.created";
    public static final String MENU_UPDATED = "menus.updated";
    /* meals table */
    public static final String MEAL_ID = "meals.id";
    public static final String MEAL_TITLE = "meals.title";
    public static final String MEAL_IMAGE = "meals.image";
    public static final String MEAL_PRICE = "meals.price";
    public static final String MEAL_RECIPE = "meals.recipe";
    public static final String MEAL_CREATED = "meals.created";
    public static final String MEAL_ACTIVE = "meals.active";
    /* meal_types table */
    public static final String MEAL_TYPES_TYPE = "meal_types.type";
    /* menu_types table */
    public static final String MENU_TYPES_TYPE = "menu_types.type";
    /* carts table */
    public static final String MEAL_QUANTITY = "carts.quantity";
    /* orders table */
    public static final String ORDER_ID = "orders.id";
    public static final String ORDER_CREATED = "orders.created";
    public static final String ORDER_CASH = "orders.cash";
    public static final String ORDER_USER_ID = "orders.user_id";
    public static final String DELIVERY_TIME = "orders.delivery_time";
    /* order_statuses table */
    public static final String ORDER_STATUS = "order_statuses.status";
    /* orders_have_meals table */
    public static final String ORDER_QUANTITY = "orders_have_meals.quantity";

    private ColumnName() {
    }
}