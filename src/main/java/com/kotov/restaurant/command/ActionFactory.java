package com.kotov.restaurant.command;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionFactory {
    private static final Logger logger = LogManager.getLogger();

    public static Command getCommand(String commandStr) {
        Command command;
        try {
            CommandType commandType = CommandType.valueOf(commandStr.toUpperCase());
            command = commandType.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Nonexistent command:", e);
            command = CommandType.DEFAULT_COMMAND.getCurrentCommand();                                              // to do NONEXISTENT_COMMAND
        }
        return command;
    }
}