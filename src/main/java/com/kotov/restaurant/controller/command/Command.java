package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    Router execute(HttpServletRequest request) throws CommandException;
}