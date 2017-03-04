package com.kevin_mic.aqua.rest;

import com.google.inject.Binder;
import com.google.inject.Provides;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class AquaControllerModule implements com.google.inject.Module {
    private static DBI jdbi = null;

    public void configure(Binder binder) {

    }

    @Provides
    public DBI get(Environment environment, AquaControllerConfig config) {
        if (jdbi == null) {
            synchronized (this) {
                if (jdbi == null) {
                    final DBIFactory factory = new DBIFactory();
                    jdbi = factory.build(environment, config.getDataSourceFactory(null), "postgres");
                }
            }
        }
        return jdbi;
    }
}
