package com.kotov.restaurant.command;

import com.kotov.restaurant.command.impl.DefaultCommand;
import com.kotov.restaurant.command.impl.AuthenticationCommand;
import com.kotov.restaurant.command.impl.LogoutCommand;
import com.kotov.restaurant.command.impl.RegistrationCommand;
import com.kotov.restaurant.command.impl.movement.*;

enum CommandType {
    SIGN_IN(new AuthenticationCommand()),
    LOGOUT(new LogoutCommand()),
    REGISTRATION(new RegistrationCommand()),
    GO_TO_REGISTRATION_COMMAND(new GoToRegistrationCommand()),
    GO_TO_MAIN_COMMAND(new GoToMainCommand()),
    GO_TO_MENU_COMMAND(new GoToMenuCommand()),
    GO_TO_ORDERS_COMMAND(new GoToOrdersCommand()),
    GO_TO_BOOKING_COMMAND(new GoToBookingCommand()),
    GO_TO_SETTINGS_COMMAND(new GoToSettingsCommand()),
    GO_TO_REVIEWS_COMMAND(new GoToReviewsCommand()),
    GO_TO_CART_COMMAND(new GoToCartCommand()),
    DEFAULT_COMMAND(new DefaultCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    Command getCurrentCommand() {
        return command;
    }
}