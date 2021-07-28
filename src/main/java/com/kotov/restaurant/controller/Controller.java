package com.kotov.restaurant.controller;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.ActionFactory;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.model.pool.ConnectionPool;
import jakarta.servlet.ServletException;
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

@WebServlet(name = "Controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        ConnectionPool.getInstance();
        logger.log(Level.INFO, "Connection pool has been initialized");
    }

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
            switch (router.getRoute()) {
                case FORWARD -> getServletContext().getRequestDispatcher(router.getPagePath()).forward(req, resp);
                case REDIRECT -> resp.sendRedirect(router.getPagePath());
            }
        } catch (CommandException e) {
            resp.sendError(INTERNAL_SERVER_ERROR);
            logger.log(Level.ERROR, "Internal error has occurred:", e);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "Connection pool has destroyed");
    }
}