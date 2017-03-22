package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

import static org.junit.Assert.assertEquals;
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
                "update " + Device.TABLE_NAME + " set pinId = null",
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
        int pinId = getPinId(supplierId);

        Device createDevice = createDevice(pinId);
        tested.addDevice(createDevice);
        int deviceId = createDevice.getDeviceId();

        Device findDevice = tested.getDevice(deviceId);
        assertEquals(createDevice, findDevice);

        Pin pin = pinSupplierDao.getPins(supplierId).get(0);
        assertEquals(new Integer(deviceId), pin.getOwnedByDeviceId());

        assertEquals(1, tested.getAllDevices().size());

        tested.removeDevice(deviceId);

        findDevice = tested.getDevice(deviceId);
        assertNull(findDevice);

        pin = pinSupplierDao.getPins(supplierId).get(0);
        assertNull(pin.getOwnedByDeviceId());

        deletePin(supplierId);
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
        int pinId = getPinId(supplierId);
        {
            Device device1 = createDevice(pinId);
            tested.addDevice(device1);
        }

        Device device2 = new Device();
        device2.setName("NAME");
        device2.setPinId(pinId);
        device2.setHardwareId("HWD");
        device2.setType(DeviceType.DosingPumpPeristalticStepper);

        try {
            tested.addDevice(device2);
            fail("We should have failed");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Pin Already In Use"));
        }
    }

    @Test
    public void test_devicePinFK() {
        int supplierId = createPin();
        int pinId = getPinId(supplierId);

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
        createDevice.setPinId(pinId);
        createDevice.setHardwareId("HWD");
        createDevice.setType(DeviceType.DosingPumpPeristalticStepper);
        return createDevice;
    }

    private void deletePin(int supplierId) {
        pinSupplierDao.deleteSupplier(supplierId);
    }

    private int createPin() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());
        pinSupplier.setHardwareId("HARDWAREID");
        pinSupplier.setSupplierType(PinSupplierType.RASBERRY_PI);
        pinSupplier.setName("PI");
        pinSupplierDao.insertSupplier(pinSupplier);
        return pinSupplier.getPinSupplierId();
    }

    private int getPinId(int supplierId) {
        return pinSupplierDao.getPins(supplierId).get(0).getPinId();
    }


}
