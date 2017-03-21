package com.kevin_mic.aqua.rest;

import com.kevin_mic.aqua.service.TestJob;
import io.dropwizard.lifecycle.Managed;
import org.quartz.Scheduler;

import javax.inject.Inject;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerStartup implements Managed {
    Scheduler scheduler;

    @Inject
    public SchedulerStartup(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void start() throws Exception {
        scheduler.start();
        scheduler.scheduleJob(
                newJob(TestJob.class).withIdentity("test").build(),
                newTrigger().withIdentity("testtrigger").startNow().withSchedule(
                        simpleSchedule().withIntervalInSeconds(5).repeatForever()
                ).build()
        );
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown(true);
    }
}