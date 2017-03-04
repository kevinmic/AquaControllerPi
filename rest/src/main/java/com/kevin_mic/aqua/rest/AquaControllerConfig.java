package com.kevin_mic.aqua.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.flyway.FlywayConfiguration;
import io.dropwizard.flyway.FlywayFactory;

public class AquaControllerConfig extends Configuration implements DatabaseConfiguration, FlywayConfiguration {
    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory;

    @JsonProperty("flyway")
    private FlywayFactory flywayFactory;

    public DataSourceFactory getDataSourceFactory(Configuration configuration) {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public FlywayFactory getFlywayFactory(Configuration configuration) {
        return flywayFactory;
    }

    public void setFlywayFactory(FlywayFactory flywayFactory) {
        this.flywayFactory = flywayFactory;
    }
}
