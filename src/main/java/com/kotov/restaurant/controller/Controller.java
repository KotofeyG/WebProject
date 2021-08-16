package com.kotov.restaurant.controller;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.ActionFactory;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.kotov.restaurant.controller.command.ParamName.COMMAND;
import static com.kotov.restaurant.controller.command.ParamName.INTERNAL_SERVER_ERROR;

@WebServlet(name = "controller", urlPatterns = "/controller")
@MultipartConfig(fileSizeThreshold = 1024*1024, maxFileSize = 5*1024*1024, maxRequestSize = 5*5*1024*1024)
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandStr = req.getParameter(COMMAND);
        Command command = ActionFactory.getCommand(commandStr);
        try {
            Router router = command.execute(req);
            if (router.hasError()) {
                resp.sendError(router.getErrorCode());
            } else if (router.getRouterType() == Router.RouteType.REDIRECT){
                resp.sendRedirect(router.getPagePath());
            } else {
                getServletContext().getRequestDispatcher(router.getPagePath()).forward(req, resp);
            }
        } catch (CommandException e) {
            logger.log(Level.ERROR, "Internal error has occurred:", e);
            resp.sendError(INTERNAL_SERVER_ERROR);
        }
    }
}