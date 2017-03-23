package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;

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

    public Device update(int deviceId, DeviceUpdate deviceUpdate) {
        Device device = findById(deviceId);

        device.setName(deviceUpdate.getName());
        device.setHardwareId(deviceUpdate.getHardwareId());
        device.setPins(deviceUpdate.getPins());

        deviceValidator.validate(device);
        deviceValidator.validatePinTypes(device.getType(), device.getPins());
        deviceValidator.validatePinsNotUsed(device.getDeviceId(), device.getPins());

        return deviceDao.updateDevice(device);
    }

    public void delete(int deviceId) {
        deviceDao.removeDevice(deviceId);
    }
}
