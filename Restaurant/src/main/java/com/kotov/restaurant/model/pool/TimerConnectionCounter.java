package com.kotov.restaurant.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

class TimerConnectionCounter extends TimerTask {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run() {
        logger.log(Level.INFO, "Connection pool check is running");
        ConnectionPool pool = ConnectionPool.getInstance();
        pool.addMissingConnections();
    }
}