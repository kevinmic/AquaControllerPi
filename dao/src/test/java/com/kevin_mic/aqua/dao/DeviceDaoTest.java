package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.DevicePin;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.types.PinType;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeviceDaoTest extends BaseTest {
    private DeviceDao tested;
    private PinSupplierDao pinSupplierDao;

    @Before
    public void beforeMethod() {
        tested = new DeviceDao(dbi);
        pinSupplierDao = new PinSupplierDao(dbi);
    }

    @Override
    public String[] cleanupSql() {
        return new String[] {
                "update " + Pin.TABLE_NAME + " set ownedByDeviceId = null",
                "delete from " + DevicePin.TABLE_NAME,
                "delete from " + Pin.TABLE_NAME,
                "delete from " + PinSupplier.TABLE_NAME,
                "delete from " + Device.TABLE_NAME
        };
    }

    @Test
    public void test_crud_noPin() {
        Device device = new Device();
        device.setName("NAME");
        device.setHardwareId("HWD");
        device.setDeviceId(1);
        device.setType(DeviceType.DosingPumpPeristalticStepper);

        tested.addDevice(device);
    }

    @Test
    public void test_crud_wPin() {
        int supplierId = createPin();
        int pinId = getPin(supplierId, 0).getPinId();

        Device createDevice = createDevice(pinId);
        tested.addDevice(createDevice);
        int deviceId = createDevice.getDeviceId();

        Device findDevice = tested.getDevice(deviceId);
        assertEquals(createDevice, findDevice);

        Pin pin = pinSupplierDao.findPin(pinId);
        assertEquals(new Integer(deviceId), pin.getOwnedByDeviceId());

        assertEquals(1, tested.getAllDevices().size());

        tested.removeDevice(deviceId);

        try {
            tested.getDevice(deviceId);
            fail();
        }
        catch (EntityNotFoundException e) {
        }

        pin = pinSupplierDao.getPins(supplierId).get(0);
        assertNull(pin.getOwnedByDeviceId());

        deletePin(supplierId);
    }

    @Test
    public void test_update() {
        int supplierId = createPin();
        int pinIdA = getPin(supplierId, 0).getPinId();

        Device createDevice = createDevice(pinIdA);
        tested.addDevice(createDevice);

        int deviceId = createDevice.getDeviceId();

        Pin pin = pinSupplierDao.findPin(pinIdA);
        assertEquals(new Integer(deviceId), pin.getOwnedByDeviceId());


        int pinIdB = getPin(supplierId, 1).getPinId();
        List<DevicePin> pins = new ArrayList<>();
        pins.add(new DevicePin(pinIdB, -1, PinType.SN74HC595_Clock));

        Device updateDevice = tested.getDevice(deviceId);
        updateDevice.setPins(pins);
        updateDevice.setName("NEW_NAME");
        updateDevice.setHardwareId("NEW_HARDWARE");
        updateDevice.setType(DeviceType.I2C_BUS);

        Device checkDevice = tested.updateDevice(updateDevice);
        assertNotEquals(checkDevice, updateDevice);
        assertEquals(checkDevice.getHardwareId(), updateDevice.getHardwareId());
        assertEquals(checkDevice.getName(), updateDevice.getName());
        assertNotEquals(checkDevice.getType(), updateDevice.getType());

        pin = pinSupplierDao.findPin(pinIdA);
        assertNull(pin.getOwnedByDeviceId());

        pin = pinSupplierDao.findPin(pinIdB);
        assertEquals(new Integer(deviceId), pin.getOwnedByDeviceId());
    }

    @Test
    public void test_devicePinFK_pinMustExist() {
        Device createDevice = createDevice(1111);

        try {
            tested.addDevice(createDevice);
            fail("We should have had a constraint violation");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Referential integrity constraint violation"));
        }
    }


    @Test
    public void test_devicePinFK_pinAlreadyUsed() {
        int supplierId = createPin();
        int pinId = getPin(supplierId, 0).getPinId();
        {
            Device device1 = createDevice(pinId);
            tested.addDevice(device1);
        }

        Device device2 = createDevice(pinId);
        try {
            tested.addDevice(device2);
            fail("We should have failed");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Pin Already In Use"));
        }
    }

    private void addPin(Device device, int pinId, PinType type) {
        List<DevicePin> pins = device.getPins();
        if (pins == null) {
            pins = new ArrayList<>();
            device.setPins(pins);
        }

        pins.add(new DevicePin(pinId, -1, type));
    }

    @Test
    public void test_devicePinFK() {
        int supplierId = createPin();
        int pinId = getPin(supplierId, 0).getPinId();

        Device device1 = createDevice(pinId);
        tested.addDevice(device1);

        try {
            pinSupplierDao.deleteSupplier(supplierId);
            fail("Should not have been allowed");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Some pins are still in use"));
        }

    }

    private Device createDevice(Integer pinId) {
        Device createDevice = new Device();
        createDevice.setName("NAME");
        createDevice.setHardwareId("HWD");
        createDevice.setType(DeviceType.DosingPumpPeristalticStepper);
        addPin(createDevice, pinId, PinType.I2C_SDA1);
        return createDevice;
    }

    private void deletePin(int supplierId) {
        pinSupplierDao.deleteSupplier(supplierId);
    }

    private int createPin() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());
        pinSupplier.setHardwareId("HARDWAREID");
        pinSupplier.setType(PinSupplierType.RASBERRY_PI);
        pinSupplier.setSubType(PinSupplierSubType.PI);
        pinSupplier.setName("PI");
        pinSupplierDao.insertSupplier(pinSupplier);
        return pinSupplier.getPinSupplierId();
    }

    private Pin getPin(int supplierId, int pinNumber) {
        return pinSupplierDao.getPins(supplierId).stream().filter(p -> p.getPinNumber() == pinNumber).findFirst().get();
    }


}
