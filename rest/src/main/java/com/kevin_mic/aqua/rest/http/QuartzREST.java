package com.kevin_mic.aqua.rest.http;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/v1/quartz")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuartzREST {
    private final Scheduler scheduler;

    @Inject
    public QuartzREST(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Path("/jobs")
    @GET
    public Set<JobKey> listJobs() throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.anyGroup());
    }

    @Path("/jobs/{groupId}")
    @GET
    public Set<JobKey> getJob(@PathParam("groupId") String groupId) throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.groupEquals(groupId));
    }

    @Path("/jobs/{groupId}/{jobId}")
    @GET
    public JobDetail getJob(@PathParam("groupId") String groupId, @PathParam("jobId") String jobId) throws SchedulerException {
        return scheduler.getJobDetail(new JobKey(jobId, groupId));
    }

    @Path("/jobs/{groupId}/{jobId}")
    @DELETE
    public void deleteJob(@PathParam("groupId") String groupId, @PathParam("jobId") String jobId) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobId, groupId));
    }

    @Path("/triggers")
    @GET
    public List<String> listTriggers() throws SchedulerException {
        return scheduler.getTriggerKeys(GroupMatcher.anyGroup()).stream()
                .map(k -> {
                    try {
                        return scheduler.getTrigger(k);
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(t -> t.getKey() + " : " + toString(t.getScheduleBuilder()))
                .collect(Collectors.toList());
    }

    private String toString(ScheduleBuilder<? extends Trigger> scheduleBuilder) {
        if (scheduleBuilder instanceof CronScheduleBuilder) {
            CronScheduleBuilder cron = (CronScheduleBuilder) scheduleBuilder;
            return "CRON: " + ((CronTriggerImpl) cron.build()).getCronExpression().toString();
        }
        else {
            return scheduleBuilder.toString();
        }
    }
}
