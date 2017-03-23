package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.DevicePin;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.PinType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

public class DeviceValidator {
    private final PinSupplierDao pinSupplierDao;

    @Inject
    public DeviceValidator(PinSupplierDao pinSupplierDao) {
        this.pinSupplierDao = pinSupplierDao;
    }

    public void validate(Device device) {
        if (StringUtils.isBlank(device.getName())) {
            throw new AquaException(ErrorType.DeviceNameRequired);
        }
        if (device.getType() == null) {
            throw new AquaException(ErrorType.DeviceTypeRequired);
        }
        if (!device.getType().isCanCreate()) {
            throw new AquaException(ErrorType.CannotCreateThisDeviceType);
        }
    }

    public void validatePinsNotUsed(int deviceId, List<DevicePin> pins) {
        for (DevicePin devicePin : pins) {
            Pin pin = pinSupplierDao.findPin(devicePin.getPinId());
            if (pin == null) {
                throw new AquaException(ErrorType.InvalidPinId);
            }
            if (pin.getOwnedByDeviceId() != null && deviceId != pin.getOwnedByDeviceId()) {
                throw new AquaException(ErrorType.PinAlreadyOwned);
            }
        }
    }

    public void validatePinTypes(DeviceType type, List<DevicePin> pins) {
        List<PinType> requiredPinTypes = type.getRequiredPinTypes();
        if (pins.size() != requiredPinTypes.size()) {
            throw new AquaException(ErrorType.WrongNumberOfPins);
        }

        long validPinCount = pins.stream()
                .map(DevicePin::getPinType)
                .filter(pinType -> requiredPinTypes.contains(pinType))
                .distinct()
                .count();

        if (validPinCount != pins.size()) {
            throw new AquaException(ErrorType.InvalidPinTypes);
        }
    }
}
