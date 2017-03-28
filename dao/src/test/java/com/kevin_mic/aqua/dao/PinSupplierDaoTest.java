package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class PinSupplierDaoTest extends BaseTest {
    private PinSupplierDao tested;

    @Before
    public void beforeMethod() {
        tested = new PinSupplierDao(dbi);
    }

    @Test
    public void test_crud() {
        PinSupplier createSupplier = getPinSupplier();
        int supplierId = createSupplier.getPinSupplierId();

        tested.addPinSupplier(createSupplier);

        PinSupplier foundSupplier = tested.getPinSupplier(supplierId);
        assertEquals(createSupplier, foundSupplier);

        List<Pin> foundPins = tested.getPins(supplierId);
        assertNotNull(foundPins);
        assertEquals(8, foundPins.size());
        Pin test_1_1 = foundPins.stream().filter(p -> p.getPinNumber() == 1).findFirst().get();
        assertEquals(supplierId, test_1_1.getPinSupplierId());
        assertNull(test_1_1.getOwnedByDeviceId());

        Pin checkPin = tested.getPin(test_1_1.getPinId());
        assertEquals(test_1_1, checkPin);

        assertEquals(1, tested.getPinSuppliers().size());

        tested.deletePinSupplier(supplierId);

        try {
            tested.getPinSupplier(supplierId);
            fail();
        }
        catch (EntityNotFoundException e) {
            // expected
        }

        foundPins = tested.getPins(supplierId);
        assertEquals(0, foundPins.size());

        try {
            tested.getPin(-1);
            fail();
        }
        catch (EntityNotFoundException e) {
            // expected
        }

    }

    @Test
    public void test_update() {
        PinSupplier pinSupplier = getPinSupplier();
        tested.addPinSupplier(pinSupplier);

        // I am modifying all fields and then checking to make sure only the ones I expected changed
        pinSupplier.setHardwareId("NEW_HRD");
        pinSupplier.setName("NEW_NAME");
        pinSupplier.setType(PinSupplierType.RASBERRY_PI);
        pinSupplier.setSubType(PinSupplierSubType.SensorArray);

        PinSupplier checkIt = tested.updatePinSupplier(pinSupplier);

        PinSupplier expectedSupplier = getPinSupplier();
        expectedSupplier.setHardwareId("NEW_HRD");
        expectedSupplier.setName("NEW_NAME");
        expectedSupplier.setPinSupplierId(pinSupplier.getPinSupplierId());

        assertNotEquals(pinSupplier, checkIt);
        assertEquals(expectedSupplier, checkIt);

    }

    private PinSupplier getPinSupplier() {
        PinSupplier createSupplier = new PinSupplier();
        createSupplier.setPinSupplierId(tested.getNextId());
        createSupplier.setHardwareId("HRD1");
        createSupplier.setName("NAME");
        createSupplier.setType(PinSupplierType.PCF8574);
        createSupplier.setSubType(PinSupplierSubType.Relay_12_VDC);
        return createSupplier;
    }

    @Override
    public String[] cleanupSql() {
        return new String[] {
                "update " + Pin.TABLE_NAME + " set ownedByDeviceId = null",
                "delete from " + DevicePin.TABLE_NAME,
                "delete from " + Pin.TABLE_NAME,
                "delete from " + PinSupplier.TABLE_NAME,
        };
    }

}