package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.dbi.DeviceDbi;
import com.kevin_mic.aqua.dbi.PinDbi;
import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.DevicePin;
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
            if (!CollectionUtils.isEmpty(device.getPins())) {
                for (DevicePin devicePin : device.getPins()) {
                    devicePin.setDeviceId(deviceId);
                    pinDbi.addDeviceOwnershipWithCheck(devicePin);
                    deviceDbi.insertDevicePin(devicePin);
                }
            }

            return null;
        });

        return device;
    }

    public Device getDevice(int deviceId) {
        Device device = getDeviceDbi().getDevice(deviceId);
        if (device != null) {
            device.setPins(getDeviceDbi().getPins(deviceId));
        }
        return device;
    }

    private DeviceDbi getDeviceDbi() {
        return dbi.onDemand(DeviceDbi.class);
    }

    public void removeDevice(int deviceId) {
        dbi.inTransaction((handle,ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            DeviceDbi deviceDbi = handle.attach(DeviceDbi.class);

            Device device = deviceDbi.getDevice_forUpdate(deviceId);
            if (device != null) {
                List<DevicePin> pins = deviceDbi.getPins(deviceId);
                if (!CollectionUtils.isEmpty(pins)) {
                    for (DevicePin devicePin : pins) {
                        pinDbi.removeDeviceOwnership(devicePin);
                    }
                }
                deviceDbi.removeAllPins(deviceId);
                deviceDbi.delete(deviceId);
            }
            return null;
        });
    }

    public List<Device> getAllDevices() {
        return getDeviceDbi().getAllDevices();
    }
}
