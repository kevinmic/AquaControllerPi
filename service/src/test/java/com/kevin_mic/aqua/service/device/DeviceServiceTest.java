package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.DevicePin;
import com.kevin_mic.aqua.model.types.DeviceType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceServiceTest {
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
        verify(deviceValidator).validatePinsNotUsed(-1, pins);
        verify(deviceValidator).validatePinTypes(DeviceType.DosingPumpPeristalticStepper, pins);
        verify(deviceDao).addDevice(device);
    }
}