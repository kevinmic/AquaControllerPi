package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceServiceTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    public static final String NAME = "NAME";
    DeviceService tested;
    DeviceValidator deviceValidator;
    DeviceDao deviceDao;

    @Before
    public void before() {
        deviceDao = mock(DeviceDao.class);
        deviceValidator = mock(DeviceValidator.class);
        tested = new DeviceService(deviceDao, deviceValidator);
    }

    @Test
    public void test_list() {
        when(deviceDao.getAllDevices()).thenReturn(new ArrayList<Device>());
        assertNotNull(tested.list());


        verify(deviceDao).getAllDevices();
    }

    @Test
    public void test_get() {
        when(deviceDao.getDevice(1)).thenReturn(new Device());
        assertNotNull(tested.findById(1));


        verify(deviceDao).getDevice(1);
    }

    @Test
    public void test_add() {
        Device device = new Device();
        device.setDeviceId(1);
        List<DevicePin> pins = new ArrayList<>();
        device.setPins(pins);
        device.setType(DeviceType.DosingPumpPeristalticStepper);

        tested.add(device);

        verify(deviceValidator).validate(device);
        verify(deviceValidator).validatePins(device);
        verify(deviceDao).addDevice(device);
    }

    @Test
    public void test_update() {
        Device device = mock(Device.class);
        List<DevicePin> pins = new ArrayList<>();

        DeviceUpdate update = new DeviceUpdate();
        update.setHardwareId(HARDWARE_ID);
        update.setName(NAME);
        update.setPins(pins);

        when(device.getDeviceId()).thenReturn(1);
        when(device.getHardwareId()).thenReturn(HARDWARE_ID);
        when(device.getPins()).thenReturn(pins);
        when(device.getType()).thenReturn(DeviceType.DosingPumpPeristalticStepper);
        when(deviceDao.getDevice(1)).thenReturn(device);

        tested.update(1, update);

        verify(device).setName(NAME);
        verify(device).setHardwareId(HARDWARE_ID);
        verify(device).setPins(pins);

        verify(deviceValidator).validate(device);
        verify(deviceValidator).validatePins(device);
        verify(deviceDao).updateDevice(device);
    }

    @Test
    public void test_delete() {
        tested.delete(1);

        verify(deviceDao).removeDevice(1);
    }
}