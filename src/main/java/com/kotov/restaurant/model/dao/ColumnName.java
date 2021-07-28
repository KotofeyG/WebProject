package com.kotov.restaurant.model.dao;

public class ColumnName {
    /* table names */
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ROLES = "roles";
    public static final String TABLE_USER_STATUSES = "user_statuses table";
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_CREDIT_CARDS = "credit_cards";
    public static final String TABLE_MENUS = "menus";
    public static final String TABLE_MEALS = "meals";
    public static final String TABLE_AVAILABLE_MEALS = "available_meals";
    public static final String TABLE_BASKETS = "baskets";
    public static final String TABLE_BASKETS_HAS_MEALS = "baskets_has_meals";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_STATUSES = "order_statuses";
    public static final String TABLE_ORDERS_HAS_MEALS = "orders_has_meals";
    public static final String TABLE_BOOKING = "booking";
    public static final String TABLE_BOOKING_STATUSES = "booking_statuses";
    public static final String TABLE_TABLE = "table";
    public static final String TABLE_TABLE_STATUSES = "table_statuses";

    /* users table */
    public static final String USER_ID = "users.id";
    public static final String LOGIN = "users.login";
    public static final String PASSWORD = "users.password";
    public static final String EMAIL_ADDRESS = "users.email_address";
    public static final String FIRST_NAME = "users.first_name";
    public static final String MIDDLE_NAME = "users.middle_name";
    public static final String LAST_NAME = "users.last_name";
    public static final String MOBILE_NUMBER = "users.mobile_number";
    public static final String REGISTERED = "users.registered";
    public static final String USER_ADDRESS_ID = "users.address_id";
    public static final String USER_ROLE_ID = "users.role_id";
    public static final String USER_STATUS_ID = "users.status_id";

    /* roles table */
    public static final String ROLE_ID = "roles.id";
    public static final String USER_ROLE = "roles.role";

    /* user_statuses table */
    public static final String STATUS_ID = "user_statuses.status.id";
    public static final String USER_STATUS = "user_statuses.status";

    /* address table */
    public static final String ADDRESS_ID = "address.id";
    public static final String CITY = "address.city";
    public static final String STREET = "address.street";
    public static final String BUILDING = "address.building";
    public static final String BLOCK = "address.block";
    public static final String FLAT = "address.flat";
    public static final String ENTRANCE = "address.entrance";
    public static final String FLOOR = "address.floor";
    public static final String INTERCOM_CODE = "address.intercom_code";

    /* credit_cards table */
    public static final String CREDIT_CARD_ID = "credit_cards.id";
    public static final String CREDIT_CARD_TYPE = "credit_cards.type";
    public static final String CREDIT_CARD_NUMBER = "credit_cards.number";
    public static final String EXPIRATION_DATE = "credit_cards.expiration_date";
    public static final String HOLDER_NAME = "credit_cards.holder_name";
    public static final String CVC = "credit_cards.cvc";
    public static final String CREDIT_CARD_USER_ID = "credit_cards.user_id";

    /* menus table */
    public static final String MENU_ID = "menus.id";
    public static final String MENU_TITLE = "menus.title";
    public static final String MENU_TYPE = "menus.type";
    public static final String MENU_CREATED = "menus.created";
    public static final String MENU_UPDATED = "menus.updated";
    public static final String MENU_USER_ID = "menus.user_id";

    /* meals table */
    public static final String MEAL_ID = "meals.id";
    public static final String MEAL_TITLE = "meals.title";
    public static final String MEAL_IMAGE = "meals.image";
    public static final String MEAL_TYPE = "meals.type";
    public static final String MEAL_PRICE = "meals.price";
    public static final String MEAL_RECIPE = "meals.recipe";
    public static final String MEAL_CREATED = "meals.created";
    public static final String MEAL_ACTIVE = "meals.active";

    /* available_meals table */
    public static final String AVAILABLE_MEAL_ID = "available_meals.id";
    public static final String AVAILABLE_MEAL_MENU_ID = "available_meals.menu_id";
    public static final String AVAILABLE_MEAL_MEAL_ID = "available_meals.meal_id";

    /* baskets table */
    public static final String BASKET_ID = "baskets.id";
    public static final String BASKET_TOTAL_PRICE = "baskets.total_price";
    public static final String BASKET_USER_ID = "baskets.user_id";

    /* baskets_has_meals table */
    public static final String BASKET_HAS_MEAL_ID = "baskets_has_meals.id";
    public static final String BASKET_HAS_MEAL_QUANTITY = "baskets_has_meals.quantity";
    public static final String BASKET_HAS_MEAL_SUB_PRICE = "baskets_has_meals.sub_price";
    public static final String BASKET_HAS_MEAL_BASKET_ID = "baskets_has_meals.basket_id";
    public static final String BASKET_HAS_MEAL_MEAL_ID = "baskets_has_meals.meal_id";

    /* orders table */
    public static final String ORDER_ID = "orders.id";
    public static final String ORDER_CREATED = "orders.created";
    public static final String ORDER_CASH = "orders.cash";
    public static final String ORDER_SHIPPING = "orders.shipping";
    public static final String ORDER_SUB_PRICE = "orders.sub_price";
    public static final String ORDER_USER_ID = "orders.user_id";
    public static final String ORDER_ORDER_STATUS_ID = "orders.order_status_id";
    public static final String ORDER_CREDIT_CARD_ID = "orders.credit_card_id";

    /* order_statuses table */
    public static final String ORDER_STATUS_ID = "order_statuses.orders.id";
    public static final String ORDER_STATUS = "order_statuses.status";

    /* orders_has_meals table */
    public static final String ORDER_HAS_MEAL_ID = "orders_has_meals.id";
    public static final String ORDER_HAS_MEAL_QUANTITY = "orders_has_meals.quantity";
    public static final String ORDER_HAS_MEAL_TOTAL_PRICE = "orders_has_meals.total_price";
    public static final String ORDER_HAS_MEAL_ORDER_ID = "orders_has_meals.order_id";
    public static final String ORDER_HAS_MEAL_MEAL_ID = "orders_has_meals.meal_id";
    public static final String ORDER_HAS_MEAL_BOOKING_ID = "orders_has_meals.booking_id";

    /* booking table */
    public static final String BOOKING_ID = "booking.orders.id";
    public static final String BOOKING_CREATED = "booking.created";
    public static final String BOOKING_UPDATED = "booking.updated";
    public static final String BOOKING_USER_ID = "booking.user_id";
    public static final String BOOKING_TABLE_ID = "booking.table_id";
    public static final String BOOKING_BOOKING_STATUS_ID = "booking.booking_status_id";

    /* booking_statuses table */
    public static final String BOOKING_STATUS_ID = "booking_statuses.orders.id";
    public static final String BOOKING_STATUS = "booking_statuses.status";

    /* tables table */
    public static final String TABLE_ID = "tables.orders.id";
    public static final String TABLE_CAPACITY = "tables.capacity";
    public static final String TABLE_TOKEN = "tables.token";
    public static final String TABLE_VIP = "tables.vip";
    public static final String TABLE_TABLE_STATUS_ID = "tables.table_status_id";

    /* table_statuses table */
    public static final String TABLE_STATUS_ID = "table_statuses.orders.id";
    public static final String TABLE_STATUS = "table_statuses.status";

    /* param indexes for statement */
    public static final int FIRST_PARAM_INDEX = 1;
    public static final int SECOND_PARAM_INDEX = 2;
    public static final int THIRD_PARAM_INDEX = 3;
    public static final int FOURTH_PARAM_INDEX = 4;
    public static final int FIFTH_PARAM_INDEX = 5;
    public static final int SIXTH_PARAM_INDEX = 6;
    public static final int SEVENTH_PARAM_INDEX = 7;

    private ColumnName() {
    }
}