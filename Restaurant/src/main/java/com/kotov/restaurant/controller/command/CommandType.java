package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.command.impl.common.*;
import com.kotov.restaurant.controller.command.impl.client.*;
import com.kotov.restaurant.controller.command.impl.manager.*;
import com.kotov.restaurant.controller.command.impl.admin.UserListActionCommand;
import com.kotov.restaurant.controller.command.impl.admin.UserManagementCommand;

import java.util.EnumSet;

import static com.kotov.restaurant.model.entity.User.*;
import static com.kotov.restaurant.model.entity.User.Role.*;

/**
 * @author Denis Kotov
 *
 * The enum Command type.
 */
public enum CommandType {
    /* common commands */
    GO_TO_MAIN_COMMAND(new GoToMainCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    SHOW_PRODUCT_INFO_COMMAND(new ShowProductInfoCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    CHANGE_LOCALE_COMMAND(new ChangeLocaleCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    LOGOUT_COMMAND(new LogoutCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT)),
    NON_EXISTENT_COMMAND(new NonExistentCommand(), EnumSet.of(ADMIN, MANAGER, CLIENT, GUEST)),
    /* guest commands */
    GO_TO_REGISTRATION_COMMAND(new GoToRegistrationCommand(), EnumSet.of(GUEST)),
    REGISTRATION_COMMAND(new RegistrationCommand(), EnumSet.of(GUEST, ADMIN)),
    AUTHENTICATION_COMMAND(new AuthenticationCommand(), EnumSet.of(GUEST)),
    /* client commands */
    SHOW_USER_ORDERS_COMMAND(new ShowUserOrdersCommand(), EnumSet.of(CLIENT)),
    ORDER_MEALS_COMMAND(new OrderMealsCommand(), EnumSet.of(CLIENT)),
    ORDER_ACTION_COMMAND(new OrderActionCommand(), EnumSet.of(CLIENT)),
    CART_MANAGEMENT_COMMAND(new CartManagementCommand(), EnumSet.of(CLIENT)),
    ADD_MEAL_TO_CART_COMMAND(new AddMealToCartCommand(), EnumSet.of(CLIENT)),
    DELETE_MEALS_FROM_CART_COMMAND(new DeleteMealsFromCartCommand(), EnumSet.of(CLIENT)),
    DELETE_USER_ADDRESS_COMMAND(new DeleteUserAddressCommand(), EnumSet.of(CLIENT)),
    SETTINGS_MANAGEMENT_COMMAND(new SettingsManagementCommand(), EnumSet.of(CLIENT)),
    ADD_NEW_ADDRESS_COMMAND(new AddNewAddressCommand(), EnumSet.of(CLIENT)),
    CHANGE_PERSONAL_DATA_COMMAND(new ChangePersonalDataCommand(), EnumSet.of(CLIENT)),
    CHANGE_USER_PASSWORD_COMMAND(new ChangeUserPassword(), EnumSet.of(CLIENT)),
    /* manager commands */
    MEAL_MANAGEMENT_COMMAND(new MealManagementCommand(), EnumSet.of(MANAGER)),
    MEAL_LIST_ACTION_COMMAND(new MealListActionCommand(), EnumSet.of(MANAGER)),
    MEAL_CREATION_COMMAND(new MealCreationCommand(), EnumSet.of(MANAGER)),
    MENU_MANAGEMENT_COMMAND(new MenuManagementCommand(), EnumSet.of(MANAGER)),
    SHOW_MENU_CREATION_INFO_COMMAND(new ShowMenuCreationInfoCommand(), EnumSet.of(MANAGER)),
    MENU_CREATION_COMMAND(new MenuCreationCommand(), EnumSet.of(MANAGER)),
    SHOW_MENU_DETAILS_COMMAND(new ShowMenuDetailsCommand(), EnumSet.of(MANAGER)),
    UPDATE_MENU_TITLE_COMMAND(new UpdateMenuTitleCommand(), EnumSet.of(MANAGER)),
    UPDATE_MENU_MEALS_COMMAND(new UpdateMenuMealsCommand(), EnumSet.of(MANAGER)),
    ADD_MENU_TO_MAIN_PAGE_COMMAND(new AddMenuToMainPageCommand(), EnumSet.of(MANAGER)),
    MENU_DELETE_COMMAND(new MenuDeleteCommand(), EnumSet.of(MANAGER)),
    SHOW_ACTIVE_ORDERS_COMMAND(new ShowActiveOrdersCommand(), EnumSet.of(MANAGER)),
    CHANGE_ORDER_STATUS_COMMAND(new ChangeOrderStatusCommand(), EnumSet.of(MANAGER)),
    /* admin commands */
    USER_MANAGEMENT_COMMAND(new UserManagementCommand(), EnumSet.of(ADMIN)),
    USER_LIST_ACTION_COMMAND(new UserListActionCommand(), EnumSet.of(ADMIN));

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