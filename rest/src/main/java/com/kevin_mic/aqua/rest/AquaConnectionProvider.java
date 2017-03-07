package com.kevin_mic.aqua.rest;

import org.quartz.utils.ConnectionProvider;
import org.skife.jdbi.v2.DBI;

import java.sql.Connection;
import java.sql.SQLException;

public class AquaConnectionProvider implements ConnectionProvider {
    DBI dbi;

    public AquaConnectionProvider() {
        this.dbi = Main.getBean(DBI.class);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dbi.open().getConnection();
    }

    @Override
    public void shutdown() throws SQLException {

    }

    @Override
    public void initialize() throws SQLException {

    }
}
