package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PinSupplierServiceTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    PinSupplierService tested;
    PinSupplierDao supplierDao;
    PinSupplierValidator validator;

    @Before
    public void before() {
        supplierDao = mock(PinSupplierDao.class);
        validator = mock(PinSupplierValidator.class);

        tested = new PinSupplierService(supplierDao, validator);
    }

    @Test
    public void test_add() {
        PinSupplier supplier = new PinSupplier();
        supplier.setPinSupplierId(55);
        supplier.setHardwareId(HARDWARE_ID);

        when(supplierDao.insertSupplier(supplier)).thenReturn(supplier);
        assertNotNull(tested.add(supplier));

        verify(validator).validate(supplier);
        verify(validator).validateHardwareConnected(supplier);
        verify(validator).validateHardwareIdNotUsed(-1, HARDWARE_ID);

        verify(supplierDao).insertSupplier(supplier);
    }

    @Test
    public void test_findById() {
        when(supplierDao.getSupplier(1)).thenReturn(new PinSupplier());
        assertNotNull(tested.findById(1));

        verify(supplierDao).getSupplier(1);
    }

    @Test
    public void test_list() {
        when(supplierDao.getPinSuppliers()).thenReturn(new ArrayList<PinSupplier>());
        assertNotNull(tested.list());

        verify(supplierDao).getPinSuppliers();
    }

    @Test
    public void test_listPins() {
        when(supplierDao.getPins(1)).thenReturn(new ArrayList<Pin>());
        assertNotNull(tested.listPins(1));

        verify(supplierDao).getPins(1);
    }

}
