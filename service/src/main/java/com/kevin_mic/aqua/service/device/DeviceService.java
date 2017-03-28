package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;

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

        return deviceDao.addDevice(device);
    }

    public Device updateDevice(int deviceId, DeviceUpdate deviceUpdate) {
        Device device = getDevice(deviceId);

        device.setName(deviceUpdate.getName());
        device.setHardwareId(deviceUpdate.getHardwareId());
        device.setPins(deviceUpdate.getPins());

        deviceValidator.validate(device);
        deviceValidator.validatePins(device);

        return deviceDao.updateDevice(device);
    }

    public void deleteDevice(int deviceId) {
        deviceDao.deleteDevice(deviceId);
    }
}
