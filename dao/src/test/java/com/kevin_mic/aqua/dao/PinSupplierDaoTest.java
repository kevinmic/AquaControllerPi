package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.entity.Device;
import com.kevin_mic.aqua.entity.Pin;
import com.kevin_mic.aqua.entity.PinSupplier;
import com.kevin_mic.aqua.types.PinSupplierType;
import com.kevin_mic.aqua.types.Voltage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PinSupplierDaoTest extends BaseTest {
    private PinSupplierDao dao;

    @Before
    public void beforeMethod() {
        dao = new PinSupplierDao(dbi);
    }

    @Test
    public void test_crud() {
        PinSupplier createSupplier = getPinSupplier();
        int supplierId = createSupplier.getPinSupplierId();

        List<String> pins = new ArrayList<>();
        pins.add("TEST_1_1");
        pins.add("TEST_1_2");
        pins.add("TEST_1_3");
        pins.add("TEST_1_4");
        dao.insertSupplier(createSupplier, pins);

        PinSupplier foundSupplier = dao.getSupplier(supplierId);
        assertEquals(createSupplier, foundSupplier);

        List<Pin> foundPins = dao.getPins(supplierId);
        assertNotNull(foundPins);
        assertEquals(4, foundPins.size());
        Pin test_1_1 = foundPins.stream().filter(p -> p.getPinId().equals("TEST_1_1")).findFirst().get();
        assertEquals(supplierId, test_1_1.getPinSupplierId());
        assertNull(test_1_1.getOwnedByDeviceId());

        dao.deleteSupplier(supplierId);

        foundSupplier = dao.getSupplier(supplierId);
        assertNull(foundSupplier);

        foundPins = dao.getPins(supplierId);
        assertEquals(0, foundPins.size());
    }

    private PinSupplier getPinSupplier() {
        PinSupplier createSupplier = new PinSupplier();
        createSupplier.setPinSupplierId(dao.getNextId());
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