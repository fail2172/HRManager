package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolInitializeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionService connectionService = (ConnectionService) ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL);
        connectionService.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionService connectionService = (ConnectionService) ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL);
        connectionService.shutDown();
    }
}
