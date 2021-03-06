package com.kevin_mic.aqua.dao;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaMapper;
import com.kevin_mic.aqua.model.dbobj.ActionDB;
import com.kevin_mic.aqua.model.dbobj.ActionDevice;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.BeforeClass;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public abstract class BaseTest {
    static Environment env;
    static DBI dbi;
    static ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @BeforeClass
    public static void before() {
        env = new Environment("test-env", new JodaMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(env, getDataSourceFactory(), "test");

        ManagedDataSource flyway_auto_migration = getDataSourceFactory().build(null, "Flyway_auto_migration");

        Flyway flyway = new Flyway();
        flyway.setDataSource(flyway_auto_migration);
        flyway.migrate();
    }

    static DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.h2.Driver");
        dataSourceFactory.setUrl("jdbc:h2:mem:testdb;MODE=POSTGRESQL");
        dataSourceFactory.setUser("sa");
        dataSourceFactory.setPassword("");
        return dataSourceFactory;
    }

    @Before
    public void truncateTables() {
        String[] tablesToTruncate = cleanupSql();
        if (tablesToTruncate != null) {
            for (String query : tablesToTruncate) {
                try (Handle open = dbi.open()) {
                    open.createStatement(query).execute();
                }
            }
        }
    }

    public String[] cleanupSql() {
        return new String[]{
                "update " + Pin.TABLE_NAME + " set ownedByDeviceId = null",
                "delete from " + ActionDevice.TABLE_NAME,
                "delete from " + ActionDB.TABLE_NAME,
                "delete from " + DevicePin.TABLE_NAME,
                "delete from " + Device.TABLE_NAME,
                "delete from " + Pin.TABLE_NAME,
                "delete from " + PinSupplier.TABLE_NAME,
        };
    }
}
