package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;

import javax.inject.Inject;
import java.util.List;

public class ActionService {
    private final ActionDao actionDao;
    private final ActionValidator actionValidator;

    @Inject
    public ActionService(ActionDao actionDao, ActionValidator actionValidator) {
        this.actionDao = actionDao;
        this.actionValidator = actionValidator;
    }

    public <T extends ActionInterface> T addAction(T action) {
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);

        return (T) actionDao.insert(action);
    }

    public <T extends ActionInterface> T updateAction(T action) {
        actionValidator.validate(action);
        actionValidator.validateRequired(action);
        actionValidator.validateDevices(action);

        return (T) actionDao.update(action);
    }

    public List<ActionInterface> listActions() {
        return actionDao.getActions();
    }

    public ActionInterface getAction(int actionId) {
        return actionDao.getAction(actionId);
    }
}
