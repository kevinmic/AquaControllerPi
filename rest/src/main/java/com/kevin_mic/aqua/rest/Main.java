package com.kevin_mic.aqua.rest;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main extends Application<AquaControllerConfig> {
    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "aqua";
    }

    @Override
    public void initialize(Bootstrap<AquaControllerConfig> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(AquaControllerConfig configuration, Environment environment) {
        final TestResource resource = new TestResource();
        environment.jersey().packages(Main.class.getPackage().getName());
    }
}
