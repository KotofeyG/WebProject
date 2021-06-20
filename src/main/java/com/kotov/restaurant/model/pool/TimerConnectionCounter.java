package com.kotov.restaurant.model.pool;

import java.util.TimerTask;

class TimerConnectionCounter extends TimerTask {
    @Override
    public void run() {
        ConnectionPool pool = ConnectionPool.getInstance();
        pool.addMissingConnections();
    }
}