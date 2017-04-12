package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory;
import com.kevin_mic.aqua.service.jobs.OnOffJob;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class ActionService {
    private final ActionDao actionDao;
    private final ActionValidator actionValidator;
    private final ScheduleServiceFactory scheduleServiceFactory;
    private final Scheduler scheduler;

    @Inject
    public ActionService(ActionDao actionDao, ActionValidator actionValidator, ScheduleServiceFactory scheduleServiceFactory, Scheduler scheduler) {
        this.actionDao = actionDao;
        this.actionValidator = actionValidator;
        this.scheduleServiceFactory = scheduleServiceFactory;
        this.scheduler = scheduler;
    }

    public <T extends ActionInterface> T addAction(T action) {
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);
        scheduleServiceFactory.validateSchedules(action);

        T savedAction = (T) actionDao.addAction(action);
        scheduleAction(savedAction);

        return savedAction;
    }

    private <T extends ActionInterface> void scheduleAction(T action) {
        deleteJobs(action.getActionId());
        createJobs(action.getActionId());
    }

    private <T extends ActionInterface> void createJobs(int actionId) {
        try {
            scheduler.scheduleJob(
                    newJob(OnOffJob.class).withIdentity("1", getActionGroupName(actionId)).usingJobData("actionId", actionId).build(),
                    newTrigger().withIdentity("1", getActionGroupName(actionId)).startNow().withSchedule(
                            simpleSchedule().withIntervalInSeconds(5).repeatForever()
                    ).build());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends ActionInterface> void deleteJobs(int actionId) {
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(getActionGroupName(actionId)));
            List<JobKey> jobs = new ArrayList<>();
            jobs.addAll(jobKeys);
            scheduler.deleteJobs(jobs);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private String getActionGroupName(int actionId) {
        return "action_" + actionId;
    }

    public <T extends ActionInterface> T updateAction(int actionId, T action) {
        ActionInterface foundAction = getAction(actionId);
        actionValidator.validateNotChanged(foundAction, action);
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);
        scheduleServiceFactory.validateSchedules(action);


        T savedAction = (T) actionDao.updateAction(action);
        scheduleAction(savedAction);

        return savedAction;
    }

    public List<ActionInterface> listActions() {
        return actionDao.getActions();
    }

    public ActionInterface getAction(int actionId) {
        return actionDao.getAction(actionId);
    }

    public void deleteAction(int actionId) {
        deleteJobs(actionId);
        actionDao.deleteAction(actionId);
    }
}
