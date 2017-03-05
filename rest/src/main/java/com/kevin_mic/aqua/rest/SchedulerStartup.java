package com.kevin_mic.aqua.rest;

import io.dropwizard.lifecycle.Managed;
import org.quartz.Scheduler;

import javax.inject.Inject;

public class SchedulerStartup implements Managed {
    Scheduler scheduler;

    @Inject
    public SchedulerStartup(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void start() throws Exception {
        scheduler.start();
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown(true);
    }
}
