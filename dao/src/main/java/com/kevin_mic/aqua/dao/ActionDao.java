package com.kevin_mic.aqua.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin_mic.aqua.dbi.ActionDbi;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.dbobj.ActionDB;
import com.kevin_mic.aqua.model.dbobj.ActionDevice;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ScheduleType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
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

    public <T extends ActionInterface> T addAction(T action) {
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

    public ActionInterface updateAction(ActionInterface action) {
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
                Object idField = new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);
                if (idField instanceof List) {
                    ((List<Integer>) idField).forEach(deviceId -> actionDbi.insertActionDevice(new ActionDevice(action.getActionId(), deviceId)));
                }
                else {
                    actionDbi.insertActionDevice(new ActionDevice(action.getActionId(), (Integer) idField));
                }
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e ) {
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

    public Integer getActionIdThatOwnsDevice(int deviceId) {
        ActionDevice actionDevice = getActionDbi().getActionForDeviceId(deviceId);
        if (actionDevice != null) {
            return actionDevice.getActionId();
        }

        return null;
    }

    public void deleteAction(int actionId) {
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
        actionEntity.setActionType(action.getType());

        ScheduleInterface schedule = action.findSchedule();
        if (schedule != null) {
            actionEntity.setScheduleType(schedule.getType());
        }

        try {
            actionEntity.setActionJson(mapper.writeValueAsString(action));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return actionEntity;
    }

    public List<ActionInterface> findActionsByScheduleType(ScheduleType type) {
        return getActionDbi().findActionsByScheduleType(type).stream().map(this::mapDbToModel).collect(Collectors.toList());
    }
}
