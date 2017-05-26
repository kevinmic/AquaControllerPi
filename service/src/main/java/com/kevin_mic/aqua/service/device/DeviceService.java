package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;
import com.kevin_mic.aqua.service.gpio.PinController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceService {
    private final DeviceDao deviceDao;
    private final DeviceValidator deviceValidator;
    private final PinController pinController;

    @Inject
    public DeviceService(DeviceDao deviceDao, DeviceValidator deviceValidator, PinController pinController) {
        this.deviceDao = deviceDao;
        this.deviceValidator = deviceValidator;
        this.pinController = pinController;
    }

    public List<Device> listDevices() {
        return deviceDao.getDevices();
    }

    public Device getDevice(int deviceId) {
        return deviceDao.getDevice(deviceId);
    }

    public Device addDevice(Device device) {
        device.setDeviceId(-1); // Unset the deviceid on addDevice
        deviceValidator.validate(device);
        deviceValidator.validatePins(device);
        deviceValidator.validateDefaultOn(device);

        return deviceDao.addDevice(device);
    }

    public Device updateDevice(int deviceId, DeviceUpdate deviceUpdate) {
        Device device = getDevice(deviceId);
        List<DevicePin> beforePins = device.getPins();

        device.setName(deviceUpdate.getName());
        device.setHardwareId(deviceUpdate.getHardwareId());
        device.setPins(deviceUpdate.getPins());

        deviceValidator.validate(device);
        deviceValidator.validatePins(device);
        deviceValidator.validateDefaultOn(device);

        resetPins(beforePins, deviceUpdate.getPins());
        return deviceDao.updateDevice(device);
    }

    public void deleteDevice(int deviceId) {
        try {
            Device device = deviceDao.getDevice(deviceId);
            resetPins(device.getPins(), null);

            deviceDao.deleteDevice(deviceId);
        }
        catch (EntityNotFoundException e) {
            // ignore
        }
    }

    private void resetPins(List<DevicePin> beforePins, List<DevicePin> afterPins) {
        List<Integer> pins = beforePins.stream().map(d -> d.getPinId()).collect(Collectors.toList());

        if (afterPins != null) {
            pins.removeAll(afterPins.stream().map(d -> d.getPinId()).collect(Collectors.toList()));
        }

        pinController.unProvisionPins(pins);
    }
}
