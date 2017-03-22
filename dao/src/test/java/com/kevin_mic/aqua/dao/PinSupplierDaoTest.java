package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.types.Voltage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

        tested.insertSupplier(createSupplier);

        PinSupplier foundSupplier = tested.getSupplier(supplierId);
        assertEquals(createSupplier, foundSupplier);

        List<Pin> foundPins = tested.getPins(supplierId);
        assertNotNull(foundPins);
        assertEquals(8, foundPins.size());
        Pin test_1_1 = foundPins.stream().filter(p -> p.getPinNumber() == 1).findFirst().get();
        assertEquals(supplierId, test_1_1.getPinSupplierId());
        assertNull(test_1_1.getOwnedByDeviceId());

        assertEquals(1, tested.getPinSuppliers().size());

        tested.deleteSupplier(supplierId);

        foundSupplier = tested.getSupplier(supplierId);
        assertNull(foundSupplier);

        foundPins = tested.getPins(supplierId);
        assertEquals(0, foundPins.size());

    }

    private PinSupplier getPinSupplier() {
        PinSupplier createSupplier = new PinSupplier();
        createSupplier.setPinSupplierId(tested.getNextId());
        createSupplier.setHardwareId("HRD1");
        createSupplier.setName("NAME");
        createSupplier.setSupplierType(PinSupplierType.PCF8574);
        createSupplier.setVoltage(Voltage._120_AC);
        return createSupplier;
    }

    @Override
    public String[] cleanupSql() {
        return new String[] {
                "update " + Pin.TABLE_NAME + " set ownedByDeviceId = null",
                "update " + Device.TABLE_NAME + " set pinid = null",
                "delete from " + Pin.TABLE_NAME,
                "delete from " + PinSupplier.TABLE_NAME,
        };
    }

}