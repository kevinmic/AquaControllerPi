package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.entity.Device;
import com.kevin_mic.aqua.entity.Pin;
import com.kevin_mic.aqua.entity.PinSupplier;
import com.kevin_mic.aqua.types.DeviceType;
import com.kevin_mic.aqua.types.PinSupplierType;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeviceDaoTest extends BaseTest {
    public static final String PIN_1 = "PIN_1";

    private DeviceDao dao;
    private PinSupplierDao pinSupplierDao;

    @Before
    public void beforeMethod() {
        dao = new DeviceDao(dbi);
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

        dao.addDevice(device);
    }

    @Test
    public void test_crud_wPin() {
        int supplierId = createPin();

        Device createDevice = createDevice();
        dao.addDevice(createDevice);
        int deviceId = createDevice.getDeviceId();

        Device findDevice = dao.getDevice(deviceId);
        assertEquals(createDevice, findDevice);

        Pin pin = pinSupplierDao.getPins(supplierId).get(0);
        assertEquals(new Integer(deviceId), pin.getOwnedByDeviceId());

        dao.removeDevice(deviceId);

        findDevice = dao.getDevice(deviceId);
        assertNull(findDevice);

        pin = pinSupplierDao.getPins(supplierId).get(0);
        assertNull(pin.getOwnedByDeviceId());

        deletePin(supplierId);
    }

    @Test
    public void test_devicePinFK_pinMustExist() {
        Device createDevice = createDevice();

        try {
            dao.addDevice(createDevice);
            fail("We should have had a constraint violation");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Referential integrity constraint violation"));
        }
    }


    @Test
    public void test_devicePinFK_pinAlreadyUsed() {
        createPin();
        {
            Device device1 = createDevice();
            dao.addDevice(device1);
        }

        Device device2 = new Device();
        device2.setName("NAME");
        device2.setPinId(PIN_1);
        device2.setHardwareId("HWD");
        device2.setType(DeviceType.DosingPumpPeristalticStepper);

        try {
            dao.addDevice(device2);
            fail("We should have failed");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Pin Already In Use"));
        }
    }

    @Test
    public void test_devicePinFK() {
        int supplierId = createPin();
        Device device1 = createDevice();
        dao.addDevice(device1);

        try {
            pinSupplierDao.deleteSupplier(supplierId);
            fail("Should not have been allowed");
        }
        catch (CallbackFailedException e) {
            assertTrue(e.getMessage().contains("Some pins are still in use"));
        }

    }

    private Device createDevice() {
        Device createDevice = new Device();
        createDevice.setName("NAME");
        createDevice.setPinId(PIN_1);
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
        List<String> pins = new ArrayList<>();
        pins.add(PIN_1);
        pinSupplierDao.insertSupplier(pinSupplier, pins);
        return pinSupplier.getPinSupplierId();
    }

}
