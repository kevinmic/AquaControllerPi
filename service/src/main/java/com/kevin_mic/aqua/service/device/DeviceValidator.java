package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.lang3.StringUtils;

public class DeviceValidator {
    public void validate(Device device) {
        if (StringUtils.isBlank(device.getName())) {
            throw new AquaException(ErrorType.DeviceNameRequired);
        }
        if (device.getType() == null) {
            throw new AquaException(ErrorType.DeviceTypeRequired);
        }
    }
}
