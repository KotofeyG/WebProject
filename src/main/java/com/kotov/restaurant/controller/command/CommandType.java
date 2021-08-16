package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.command.impl.admin.UserListActionCommand;
import com.kotov.restaurant.controller.command.impl.admin.UserManagementCommand;
import com.kotov.restaurant.controller.command.impl.client.*;
import com.kotov.restaurant.controller.command.impl.manager.*;
import com.kotov.restaurant.controller.command.impl.movement.*;
import com.kotov.restaurant.controller.command.impl.common.*;

import java.util.EnumSet;

import static com.kotov.restaurant.model.entity.User.*;
import static com.kotov.restaurant.model.entity.User.Role.*;

public enum CommandType {
    /* general commands */
    AUTHENTICATION_COMMAND(new AuthenticationCommand(), EnumSet.of(GUEST)),
    LOGOUT_COMMAND(new LogoutCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT)),
    REGISTRATION_COMMAND(new RegistrationCommand(), EnumSet.of(GUEST)),
    CHANGE_LOCALE_COMMAND(new ChangeLocaleCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    NON_EXISTENT_COMMAND(new NonExistentCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    /* product commands */
    SHOW_PRODUCTS_COMMAND(new ShowProductsCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    /* admin commands */
    USER_MANAGEMENT_COMMAND(new UserManagementCommand(), EnumSet.of(ADMIN)),
    USER_LIST_ACTION_COMMAND(new UserListActionCommand(), EnumSet.of(ADMIN)),
    /* manager commands */
    MEAL_CREATION_COMMAND(new MealCreationCommand(), EnumSet.of(MANAGER)),
    MENU_CREATION_COMMAND(new MenuCreationCommand(), EnumSet.of(MANAGER)),
    ADD_MENU_TO_MAIN_PAGE_COMMAND(new AddMenuToMainPageCommand(), EnumSet.of(MANAGER)),
    MEAL_LIST_ACTION_COMMAND(new MealListActionCommand(), EnumSet.of(MANAGER)),
    MENU_MANAGEMENT_COMMAND(new MenuManagementCommand(), EnumSet.of(MANAGER)),
    MENU_UPDATE_COMMAND(new MenuUpdateCommand(), EnumSet.of(MANAGER)),
    MENU_DELETE_COMMAND(new MenuDeleteCommand(), EnumSet.of(MANAGER)),
    SHOW_ACTIVE_ORDERS_COMMAND(new ShowActiveOrdersCommand(), EnumSet.of(MANAGER)),
    CHANGE_ORDER_STATUS_COMMAND(new ChangeOrderStatusCommand(), EnumSet.of(MANAGER)),
    EDIT_ORDER_COMMAND(new EditOrderCommand(), EnumSet.of(MANAGER)),
    /* client commands */
    ADD_TO_CART_COMMAND(new AddToCartCommand(), EnumSet.of(CLIENT)),
    CHECK_CART_COMMAND(new CheckCartCommand(), EnumSet.of(CLIENT)),
    DELETE_FROM_CART_COMMAND(new DeleteFromCartCommand(), EnumSet.of(CLIENT)),
    SETTING_ACTION_LIST_COMMAND(new SettingActionListCommand(), EnumSet.of(CLIENT)),
    ORDER_MEALS_COMMAND(new OrderMealsCommand(), EnumSet.of(CLIENT)),
    CHECK_ORDER_COMMAND(new CheckOrderCommand(), EnumSet.of(CLIENT)),
    ORDER_ACTION_COMMAND(new OrderActionCommand(), EnumSet.of(CLIENT)),
    /* go to commands */
    GO_TO_REGISTRATION_COMMAND(new GoToRegistrationCommand(), EnumSet.of(GUEST)),
    GO_TO_MAIN_COMMAND(new GoToMainCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    GO_TO_SETTINGS_COMMAND(new GoToSettingsCommand(), EnumSet.of(CLIENT));

    private Command command;
    private EnumSet<Role> allowedRoles;

    CommandType(Command command, EnumSet<Role> allowedRoles) {
        this.command = command;
        this.allowedRoles = allowedRoles;
    }

    public Command getCurrentCommand() {
        return command;
    }

    public EnumSet<Role> getAllowedRoles() {
        return allowedRoles;
    }
}