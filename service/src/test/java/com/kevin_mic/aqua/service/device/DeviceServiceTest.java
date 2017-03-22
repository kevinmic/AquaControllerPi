package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.Device;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
        tested.add(device);

        verify(deviceValidator).validate(device);
        verify(deviceDao).addDevice(device);
    }
}