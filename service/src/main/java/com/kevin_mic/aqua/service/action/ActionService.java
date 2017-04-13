package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory;

import javax.inject.Inject;
import java.util.List;

public class ActionService {
    private final ActionDao actionDao;
    private final ActionValidator actionValidator;
    private final ScheduleServiceFactory scheduleServiceFactory;

    @Inject
    public ActionService(ActionDao actionDao, ActionValidator actionValidator, ScheduleServiceFactory scheduleServiceFactory) {
        this.actionDao = actionDao;
        this.actionValidator = actionValidator;
        this.scheduleServiceFactory = scheduleServiceFactory;
    }

    public <T extends ActionInterface> T addAction(T action) {
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);
        scheduleServiceFactory.validateSchedules(action);

        T savedAction = (T) actionDao.addAction(action);
        scheduleServiceFactory.updateSchedules(null, savedAction);

        return savedAction;
    }

    public <T extends ActionInterface> T updateAction(int actionId, T action) {
        ActionInterface foundAction = getAction(actionId);
        actionValidator.validateNotChanged(foundAction, action);
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);
        scheduleServiceFactory.validateSchedules(action);


        T savedAction = (T) actionDao.updateAction(action);
        scheduleServiceFactory.updateSchedules(foundAction, savedAction);

        return savedAction;
    }

    public List<ActionInterface> listActions() {
        return actionDao.getActions();
    }

    public ActionInterface getAction(int actionId) {
        return actionDao.getAction(actionId);
    }

    public void deleteAction(int actionId) {
        scheduleServiceFactory.deleteSchedules(actionId);
        actionDao.deleteAction(actionId);
    }
}
