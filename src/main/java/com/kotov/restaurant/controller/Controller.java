package com.kotov.restaurant.controller;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.command.ActionFactory;
import com.kotov.restaurant.command.PagePath;
import com.kotov.restaurant.command.ParamName;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "Controller", urlPatterns = {"/controller", "*.res"})
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
        String commandStr = req.getParameter(ParamName.COMMAND);
        Command command = ActionFactory.getCommand(commandStr);
        try {
            Router router = command.execute(req);
            switch (router.getRoute()) {
                case FORWARD -> getServletContext().getRequestDispatcher(router.getPagePath()).forward(req, resp);
                case REDIRECT -> resp.sendRedirect(router.getPagePath());
            }
        } catch (CommandException e) {
            resp.sendRedirect(PagePath.INTERNAL_SERVER_ERROR_PAGE);                                                     //to do INTERNAL_SERVER_ERROR_PAGE
            logger.log(Level.ERROR, "Internal error has occurred:", e);
        }
    }
}