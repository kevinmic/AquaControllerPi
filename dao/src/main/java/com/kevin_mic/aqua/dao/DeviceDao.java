package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.dbi.DeviceDbi;
import com.kevin_mic.aqua.dbi.PinDbi;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import java.util.List;

public class DeviceDao {
    private DBI dbi;

    @Inject
    public DeviceDao(DBI dbi) {
        this.dbi = dbi;
    }

    public Device addDevice(Device device) {
        dbi.inTransaction((handle,ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            DeviceDbi deviceDbi = handle.attach(DeviceDbi.class);

            int deviceId = deviceDbi.insert(device);
            device.setDeviceId(deviceId);
            addDevicePins(deviceId, device.getPins(), pinDbi, deviceDbi);

            return null;
        });

        return device;
    }

    private void addDevicePins(int deviceId, List<DevicePin> pins, PinDbi pinDbi, DeviceDbi deviceDbi) {
        if (!CollectionUtils.isEmpty(pins)) {
            for (DevicePin devicePin : pins) {
                devicePin.setDeviceId(deviceId);
                pinDbi.addDeviceOwnershipWithCheck(devicePin);
                deviceDbi.insertDevicePin(devicePin);
            }
        }
    }

    public Device updateDevice(Device device) {
        dbi.inTransaction((handle, ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            DeviceDbi deviceDbi = handle.attach(DeviceDbi.class);

            deviceDbi.getDevice_forUpdate(device.getDeviceId());
            deletePinsConnection(device.getDeviceId(), pinDbi, deviceDbi);
            addDevicePins(device.getDeviceId(), device.getPins(), pinDbi, deviceDbi);
            deviceDbi.update(device);
            return null;
        });

        return getDevice(device.getDeviceId());
    }

    private void deletePinsConnection(int deviceId, PinDbi pinDbi, DeviceDbi deviceDbi) {
        List<DevicePin> pins = deviceDbi.getPins(deviceId);
        if (!CollectionUtils.isEmpty(pins)) {
            for (DevicePin devicePin : pins) {
                pinDbi.removeDeviceOwnership(devicePin);
            }
        }
        deviceDbi.removeAllPins(deviceId);
    }

    public Device getDevice(int deviceId) {
        Device device = getDeviceDbi().getDevice(deviceId);
        if (device == null) {
            throw new EntityNotFoundException("Device", deviceId);
        }
        device.setPins(getDeviceDbi().getPins(deviceId));
        return device;
    }

    private DeviceDbi getDeviceDbi() {
        return dbi.onDemand(DeviceDbi.class);
    }

    public void deleteDevice(int deviceId) {
        dbi.inTransaction((handle,ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            DeviceDbi deviceDbi = handle.attach(DeviceDbi.class);

            deviceDbi.getDevice_forUpdate(deviceId);
            deletePinsConnection(deviceId, pinDbi, deviceDbi);
            deviceDbi.delete(deviceId);

            return null;
        });
    }

    public List<Device> getDevices() {
        return getDeviceDbi().getAllDevices();
    }

}
