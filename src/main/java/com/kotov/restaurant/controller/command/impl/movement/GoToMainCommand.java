package com.kotov.restaurant.controller.command.impl.movement;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.kotov.restaurant.controller.command.ParamName.USER;
import static com.kotov.restaurant.controller.command.PagePath.MAIN_PAGE;
import static com.kotov.restaurant.controller.command.PagePath.ADMIN_MAIN_PAGE;

public class GoToMainCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Router router = new Router();
        User user = (User) session.getAttribute(USER);
        if (user != null && user.getRole().equals(User.Role.ADMIN)) {
            router.setPagePath(ADMIN_MAIN_PAGE);
        } else {
            router.setPagePath(MAIN_PAGE);
        }
        return router;
    }
}