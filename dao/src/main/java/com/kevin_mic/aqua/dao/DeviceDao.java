package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.dbi.DeviceDbi;
import com.kevin_mic.aqua.dbi.PinDbi;
import com.kevin_mic.aqua.model.Device;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;

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
            if (device.getPinId() != null) {
                pinDbi.addDeviceOwnershipWithCheck(device.getPinId(), device.getDeviceId());
            }

            return null;
        });

        return device;
    }

    public Device getDevice(int deviceId) {
        return dbi.onDemand(DeviceDbi.class).getDevice(deviceId);
    }

    public void removeDevice(int deviceId) {
        dbi.inTransaction((handle,ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            DeviceDbi deviceDbi = handle.attach(DeviceDbi.class);

            Device device = deviceDbi.getDevice_forUpdate(deviceId);
            if (device != null) {
                if (device.getPinId() != null) {
                    pinDbi.removeDeviceOwnership(device.getPinId(), deviceId);
                }
                deviceDbi.delete(deviceId);
            }
            return null;
        });
    }
}
