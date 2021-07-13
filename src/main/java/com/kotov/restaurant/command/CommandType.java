package com.kotov.restaurant.command;

import com.kotov.restaurant.command.impl.NonexistentCommand;
import com.kotov.restaurant.command.impl.AuthenticationCommand;
import com.kotov.restaurant.command.impl.LogoutCommand;
import com.kotov.restaurant.command.impl.RegistrationCommand;

enum CommandType {
    SIGN_IN(new AuthenticationCommand()),
    LOGOUT(new LogoutCommand()),
    REGISTRATION(new RegistrationCommand()),
    NONEXISTENT_COMMAND(new NonexistentCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    Command getCurrentCommand() {
        return command;
    }
}