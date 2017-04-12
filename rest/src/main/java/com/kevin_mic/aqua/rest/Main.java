package com.kevin_mic.aqua.rest;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.kevin_mic.aqua.rest.setup.AquaControllerConfig;
import com.kevin_mic.aqua.rest.setup.AquaControllerModule;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main extends Application<AquaControllerConfig> {
    // NOTE: I have done this so that AquaConnectorProvider can gain access to the DBI
    private static GuiceBundle<AquaControllerConfig> GUICE_BUNDLE;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "aqua";
    }

    @Override
    public void initialize(Bootstrap<AquaControllerConfig> bootstrap) {
        GUICE_BUNDLE = GuiceBundle.<AquaControllerConfig>newBuilder()
                .addModule(new AquaControllerModule())
                .setConfigClass(AquaControllerConfig.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();

        bootstrap.addBundle(GUICE_BUNDLE);

        bootstrap.addBundle(new FlywayBundle<AquaControllerConfig>() {
            public DataSourceFactory getDataSourceFactory(AquaControllerConfig configuration) {
                return configuration.getDataSourceFactory(configuration);
            }

            @Override
            public FlywayFactory getFlywayFactory(AquaControllerConfig configuration) {
                return configuration.getFlywayFactory(configuration);
            }
        });
        // nothing to do yet
    }

    @Override
    public void run(AquaControllerConfig configuration, Environment environment) {
        environment.jersey().register(AquaExceptionMapper.class);
    }

    public static <T> T getBean(Class<T> clazz) {
        return GUICE_BUNDLE.getInjector().getInstance(clazz);
    }
}
