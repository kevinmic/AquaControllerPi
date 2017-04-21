package com.kevin_mic.aqua.service.device;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.PinType;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;
import com.kevin_mic.aqua.service.gpio.PinController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    PinController pinController;

    @Before
    public void before() {
        deviceDao = mock(DeviceDao.class);
        deviceValidator = mock(DeviceValidator.class);
        pinController = mock(PinController.class);
        tested = new DeviceService(deviceDao, deviceValidator, pinController);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(deviceDao, deviceValidator);
    }

    @Test
    public void test_list() {
        when(deviceDao.getDevices()).thenReturn(new ArrayList<Device>());
        assertNotNull(tested.listDevices());


        verify(deviceDao).getDevices();
    }

    @Test
    public void test_get() {
        when(deviceDao.getDevice(1)).thenReturn(new Device());
        assertNotNull(tested.getDevice(1));


        verify(deviceDao).getDevice(1);
    }

    @Test
    public void test_add() {
        Device device = new Device();
        device.setDeviceId(1);
        List<DevicePin> pins = new ArrayList<>();
        device.setPins(pins);
        device.setType(DeviceType.DosingPumpPeristalticStepper);

        tested.addDevice(device);

        verify(deviceValidator).validate(device);
        verify(deviceValidator).validatePins(device);
        verify(deviceDao).addDevice(device);
    }

    @Test
    public void test_update() {
        Device device = mock(Device.class);
        List<DevicePin> beforePins = Lists.newArrayList(new DevicePin(1, 0, PinType.STEPPER_Step), new DevicePin(2, 0, PinType.STEPPER_Step));

        List<DevicePin> updatePins = Lists.newArrayList(new DevicePin(2, 0, PinType.STEPPER_Step), new DevicePin(3, 0, PinType.STEPPER_Step));

        DeviceUpdate update = new DeviceUpdate();
        update.setHardwareId(HARDWARE_ID);
        update.setName(NAME);
        update.setPins(updatePins);

        when(device.getDeviceId()).thenReturn(1);
        when(device.getHardwareId()).thenReturn(HARDWARE_ID);
        when(device.getPins()).thenReturn(beforePins);
        when(device.getType()).thenReturn(DeviceType.DosingPumpPeristalticStepper);
        when(deviceDao.getDevice(1)).thenReturn(device);

        tested.updateDevice(1, update);

        verify(device).setName(NAME);
        verify(device).setHardwareId(HARDWARE_ID);
        verify(device).setPins(updatePins);

        verify(deviceValidator).validate(device);
        verify(deviceValidator).validatePins(device);
        verify(deviceDao).updateDevice(device);
        verify(deviceDao).getDevice(1);

        verify(pinController).unProvisionPins(Lists.newArrayList(1));
    }

    @Test
    public void test_delete() {
        Device device = mock(Device.class);
        List<DevicePin> beforePins = Lists.newArrayList(new DevicePin(1, 0, PinType.STEPPER_Step), new DevicePin(2, 0, PinType.STEPPER_Step));

        when(device.getPins()).thenReturn(beforePins);
        when(deviceDao.getDevice(1)).thenReturn(device);

        tested.deleteDevice(1);

        verify(deviceDao).getDevice(1);
        verify(deviceDao).deleteDevice(1);
        verify(pinController).unProvisionPins(Lists.newArrayList(1, 2));
    }

    @Test
    public void test_delete_notFound() {
        when(deviceDao.getDevice(1)).thenThrow(new EntityNotFoundException("",1));
        tested.deleteDevice(1);
        verify(deviceDao).getDevice(1);
    }
}