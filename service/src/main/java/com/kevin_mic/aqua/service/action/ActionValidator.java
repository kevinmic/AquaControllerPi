package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.device.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.inject.Inject;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionValidator {
    private final DeviceService deviceService;
    private final ActionDao actionDao;

    @Inject
    public ActionValidator(DeviceService deviceService, ActionDao actionDao) {
        this.deviceService = deviceService;
        this.actionDao = actionDao;
    }

    public void validate(ActionInterface action) {
        if (StringUtils.isBlank(action.getName())) {
            throw new AquaException(ErrorType.ActionNameRequried);
        }
        if (action.getActionType() == null) {
            throw new AquaException(ErrorType.ActionTypeRequired);
        }
    }

    public void validateRequired(ActionInterface action) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), Required.class);
        fields.forEach(field -> {
            try {
                Object value = new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);
                if (value == null) {
                    throw new AquaException(ErrorType.ActionFieldCannotBeNull, field.getName());
                }
                else if (value instanceof List && ((List) value).isEmpty()) {
                    throw new AquaException(ErrorType.ActionFieldCannotBeEmpty, field.getName());
                }
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void validateDevices(ActionInterface action) {
        List<Field> deviceFields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), AllowedDeviceTypes.class);
        deviceFields.forEach(field -> {
            Set<DeviceType> allowedTypes = new HashSet<DeviceType>();
            allowedTypes.addAll(Arrays.asList(field.getDeclaredAnnotation(AllowedDeviceTypes.class).value()));

            boolean owned = field.getDeclaredAnnotation(Owned.class) != null;

            try {
                Object value = new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);

                if (value != null) {
                    if (value instanceof List) {
                        ((List<Integer>) value).forEach(deviceId -> validateDevice(action, deviceId, allowedTypes, owned, field.getName()));
                    }
                    else if (value instanceof Integer){
                        validateDevice(action, (Integer) value, allowedTypes, owned, field.getName());
                    }
                    else {
                        throw new RuntimeException("Invalid DeviceId Type - " + field.getName() + " - " + action.getClass().getName());
                    }
                }
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void validateDevice(ActionInterface action, Integer deviceId, Set<DeviceType> allowedTypes, boolean owned, String fieldName) {
        Device device = deviceService.findById(deviceId);
        if (!allowedTypes.contains(device.getType())) {
            throw new AquaException(ErrorType.InvalidDeviceType, fieldName + ":" + deviceId);
        }

        Integer owningActionId = actionDao.getActionIdThatOwnsDevice(deviceId);
        if (owningActionId != null && !owningActionId.equals(action.getActionId())) {
            throw new AquaException(ErrorType.DeviceAlreadyOwnedByAnotherAction, fieldName + ":" + deviceId);
        }
    }

    public void validateNotChanged(ActionInterface foundAction, ActionInterface action) {
        if (foundAction.getActionType() != action.getActionType()) {
            throw new AquaException(ErrorType.CannotChangeActionType);
        }
    }
}
