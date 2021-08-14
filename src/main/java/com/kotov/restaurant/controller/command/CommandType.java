package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.command.impl.admin.UserListActionCommand;
import com.kotov.restaurant.controller.command.impl.admin.UserManagementCommand;
import com.kotov.restaurant.controller.command.impl.client.*;
import com.kotov.restaurant.controller.command.impl.manager.*;
import com.kotov.restaurant.controller.command.impl.movement.*;
import com.kotov.restaurant.controller.command.impl.common.*;

enum CommandType {
    /* general commands */
    AUTHENTICATION_COMMAND(new AuthenticationCommand()),
    LOGOUT_COMMAND(new LogoutCommand()),
    REGISTRATION_COMMAND(new RegistrationCommand()),
    CHANGE_LOCALE_COMMAND(new ChangeLocaleCommand()),
    NON_EXISTENT_COMMAND(new NonExistentCommand()),

    /* product commands */
    SHOW_PRODUCTS_COMMAND(new ShowProductsCommand()),

    /* admin commands */
    USER_MANAGEMENT_COMMAND(new UserManagementCommand()),
    USER_LIST_ACTION_COMMAND(new UserListActionCommand()),

    /* manager commands */
    MEAL_CREATION_COMMAND(new MealCreationCommand()),
    MENU_CREATION_COMMAND(new MenuCreationCommand()),
    ADD_MENU_TO_MAIN_PAGE_COMMAND(new AddMenuToMainPageCommand()),
    MEAL_LIST_ACTION_COMMAND(new MealListActionCommand()),
    MENU_MANAGEMENT_COMMAND(new MenuManagementCommand()),
    MENU_UPDATE_COMMAND(new MenuUpdateCommand()),
    MENU_DELETE_COMMAND(new MenuDeleteCommand()),
    SHOW_ACTIVE_ORDERS_COMMAND(new ShowActiveOrdersCommand()),
    CHANGE_ORDER_STATUS_COMMAND(new ChangeOrderStatusCommand()),
    EDIT_ORDER_COMMAND(new EditOrderCommand()),

    /* client commands */
    ADD_TO_CART_COMMAND(new AddToCartCommand()),
    CHECK_CART_COMMAND(new CheckCartCommand()),
    DELETE_FROM_CART_COMMAND(new DeleteFromCartCommand()),
    SETTING_ACTION_LIST_COMMAND(new SettingActionListCommand()),
    ORDER_MEALS_COMMAND(new OrderMealsCommand()),
    CHECK_ORDER_COMMAND(new CheckOrderCommand()),
    ORDER_ACTION_COMMAND(new OrderActionCommand()),

    /* go to commands */
    GO_TO_REGISTRATION_COMMAND(new GoToRegistrationCommand()),
    GO_TO_MAIN_COMMAND(new GoToMainCommand()),
    GO_TO_MENU_COMMAND(new GoToMenuCommand()),
    GO_TO_ORDERS_COMMAND(new GoToOrdersCommand()),
    GO_TO_BOOKING_COMMAND(new GoToBookingCommand()),
    GO_TO_SETTINGS_COMMAND(new GoToSettingsCommand()),
    GO_TO_REVIEWS_COMMAND(new GoToReviewsCommand()),
    GO_TO_CART_COMMAND(new GoToCartCommand()),
    GO_TO_MENU_EDITING_COMMAND(new GoToMenuEditingCommand()),
    GO_TO_BILL_CREATION_COMMAND(new GoToBillCreationCommand()),
    GO_TO_ORDERS_CONFIRMATION_COMMAND(new GoToOrdersConfirmationCommand()),
    GO_TO_USERS_MANAGEMENT_COMMAND(new GoToUsersManagementCommand()),
    GO_TO_REVIEWS_MANAGEMENT_COMMAND(new GoToReviewsManagementCommand()),

    GO_TO_ADDING_NEW_MEAL_COMMAND(new GoToAddingNewMealCommand()),
    GO_TO_MENU_CREATION_COMMAND(new GoToMenuCreationCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    Command getCurrentCommand() {
        return command;
    }
}