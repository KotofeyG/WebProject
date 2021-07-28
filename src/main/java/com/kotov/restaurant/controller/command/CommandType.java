package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.command.impl.admin.UserManagementCommand;
import com.kotov.restaurant.controller.command.impl.manager.*;
import com.kotov.restaurant.controller.command.impl.movement.*;
import com.kotov.restaurant.controller.command.impl.user.*;

enum CommandType {
    AUTHENTICATION_COMMAND(new AuthenticationCommand()),
    LOGOUT_COMMAND(new LogoutCommand()),
    REGISTRATION_COMMAND(new RegistrationCommand()),
    CHANGE_LOCALE_COMMAND(new ChangeLocaleCommand()),
    DEFAULT_COMMAND(new DefaultCommand()),
    USER_MANAGEMENT_COMMAND(new UserManagementCommand()),

    ADD_NEW_MEAL_COMMAND(new AddNewMealCommand()),
    MEAL_LIST_ACTION_COMMAND(new MealListActionCommand()),
    FIND_ALL_MEALS_COMMAND(new FindAllMealsCommand()),
    FIND_ALL_MENU_COMMAND(new FindAllMenuCommand()),
    ADD_NEW_MENU_COMMAND(new AddNewMenuCommand()),
    MENU_UPDATE_COMMAND(new MenuUpdateCommand()),

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