package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.Device;

import javax.inject.Inject;
import java.util.List;

public class DeviceService {
    private final DeviceDao deviceDao;
    private final DeviceValidator deviceValidator;

    @Inject
    public DeviceService(DeviceDao deviceDao, DeviceValidator deviceValidator) {
        this.deviceDao = deviceDao;
        this.deviceValidator = deviceValidator;
    }

    public List<Device> list() {
        return deviceDao.getAllDevices();
    }

    public Device findById(int deviceId) {
        return deviceDao.getDevice(deviceId);
    }

    public Device add(Device device) {
        device.setDeviceId(-1); // Unset the deviceid on add
        deviceValidator.validate(device);
        deviceValidator.validatePinTypes(device.getType(), device.getPins());
        deviceValidator.validatePinsNotUsed(device.getDeviceId(), device.getPins());

        return deviceDao.addDevice(device);
    }

    public void delete(int deviceId) {
        deviceDao.removeDevice(deviceId);
    }
}
