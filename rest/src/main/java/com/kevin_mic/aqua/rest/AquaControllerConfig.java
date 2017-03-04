package com.kevin_mic.aqua.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;

public class AquaControllerConfig extends Configuration implements DatabaseConfiguration {
    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory;

    public DataSourceFactory getDataSourceFactory(Configuration configuration) {
        return null;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }
}
