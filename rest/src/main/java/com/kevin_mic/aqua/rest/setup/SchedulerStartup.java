package com.kevin_mic.aqua.rest.setup;

import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.action.ActionService;
import com.kevin_mic.aqua.service.action.schedulevalidators.OnOffScheduleService;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

@Slf4j
public class SchedulerStartup implements Managed {
    private final Scheduler scheduler;
    private final ActionService actionService;


    @Inject
    public SchedulerStartup(Scheduler scheduler, ActionService actionService) {
        this.scheduler = scheduler;
        this.actionService = actionService;
    }

    @Override
    public void start() throws Exception {
        scheduler.start();

        triggerJobs(ScheduleType.AlwaysOn, (a,b) -> true);
        triggerJobs(ScheduleType.OnOff, (action, jobKey) -> {
            OnOffSchedule schedule = (OnOffSchedule) action.findSchedule();
            String onOffName = OnOffScheduleService.getOnOffName(schedule.isOnNow());
            return jobKey.getName().contains(onOffName);
        });
    }

    private void triggerJobs(ScheduleType type, BiFunction<ActionInterface, JobKey, Boolean> function) {
        List<ActionInterface> actions = actionService.findActionsByScheduleType(type);
        for (ActionInterface action : actions) {
            Set<JobKey> jobKeys = null;
            try {
                jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(ScheduleServiceFactory.getActionGroupName(action.getActionId())));
                for (JobKey jobKey : jobKeys) {
                    if (function.apply(action, jobKey)) {
                        scheduler.triggerJob(jobKey);
                    }
                }
            } catch (SchedulerException e) {
                log.error("Errors triggering startup job - actionId:{}", action.getActionId(), e);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        log.info("Shutdown Scheduler");
        scheduler.shutdown(false);
    }
}
