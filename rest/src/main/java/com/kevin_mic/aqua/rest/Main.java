package com.kevin_mic.aqua.rest;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main extends Application<AquaControllerConfig> {
    private GuiceBundle<AquaControllerConfig> guiceBundle;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "aqua";
    }

    @Override
    public void initialize(Bootstrap<AquaControllerConfig> bootstrap) {
        guiceBundle = GuiceBundle.<AquaControllerConfig>newBuilder()
                .addModule(new AquaControllerModule())
                .setConfigClass(AquaControllerConfig.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();

        bootstrap.addBundle(guiceBundle);

        // nothing to do yet
    }

    @Override
    public void run(AquaControllerConfig configuration, Environment environment) {
    }
}
