package com.kotov.restaurant.command.impl;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        return null;
    }
}