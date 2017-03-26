package com.kevin_mic.aqua.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin_mic.aqua.dbi.ActionDbi;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.dbobj.ActionDB;
import com.kevin_mic.aqua.model.dbobj.ActionDevice;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class ActionDao {
    private ObjectMapper mapper;

    private DBI dbi;

    @Inject
    public ActionDao(DBI dbi, ObjectMapper objectMapper) {
        this.dbi = dbi;
        this.mapper = objectMapper;
    }

    public ActionInterface insert(ActionInterface action) {
        action.setActionId(getActionDbi().getNextId());
        ActionDB actionEntity = mapModelToDB(action);

        dbi.inTransaction((handle, ts) -> {
            ActionDbi actionDbi = handle.attach(ActionDbi.class);
            actionDbi.insertAction(actionEntity);

            insertActionDevices(action, actionDbi);

            return null;
        });

        return action;
    }

    public ActionInterface update(ActionInterface action) {
        ActionDB actionEntity = mapModelToDB(action);
        dbi.inTransaction((handle, ts) -> {
            ActionDbi actionDbi = handle.attach(ActionDbi.class);
            actionDbi.updateAction(actionEntity);

            actionDbi.deleteActionDevicesForActionId(action.getActionId());
            insertActionDevices(action, actionDbi);

            return null;
        });

        return action;
    }

    private void insertActionDevices(ActionInterface action, ActionDbi actionDbi) {
        List<Field> ownedDeviceIds = FieldUtils.getFieldsListWithAnnotation(action.getClass(), Owned.class);
        ownedDeviceIds.forEach(field -> {
            try {
                Integer deviceId = Integer.parseInt(BeanUtils.getProperty(action, field.getName()));
                actionDbi.insertActionDevice(new ActionDevice(action.getActionId(), deviceId));
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<ActionInterface> getActions() {
        return getActionDbi().getActions().stream().map(this::mapDbToModel).collect(Collectors.toList());
    }

    public ActionInterface getAction(int actionId) {
        ActionDB action = getActionDbi().getAction(actionId);
        if (action == null) {
            throw new EntityNotFoundException("Action", actionId);
        }

        return mapDbToModel(action);
    }

    public void delete(int actionId) {
        dbi.inTransaction((handle, ts) -> {
            ActionDbi dbi = handle.attach(ActionDbi.class);
            dbi.deleteActionDevicesForActionId(actionId);
            dbi.delete(actionId);

            return null;
        });
    }


    private ActionDbi getActionDbi() {
        return dbi.onDemand(ActionDbi.class);
    }

    private ActionInterface mapDbToModel(ActionDB actionEntity) {
        try {
            ActionInterface action = mapper.readValue(actionEntity.getActionJson(), ActionInterface.class);
            action.setActionId(actionEntity.getActionId());
            return action;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private ActionDB mapModelToDB(ActionInterface action) {
        ActionDB actionEntity = new ActionDB();
        actionEntity.setName(action.getName());
        actionEntity.setActionId(action.getActionId());
        actionEntity.setActionType(action.getActionType());
        try {
            actionEntity.setActionJson(mapper.writeValueAsString(action));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return actionEntity;
    }
}
