package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PinSupplierServiceTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    public static final String NAME = "NAME";
    PinSupplierService tested;
    PinSupplierDao supplierDao;
    PinSupplierValidator validator;

    @Before
    public void before() {
        supplierDao = mock(PinSupplierDao.class);
        validator = mock(PinSupplierValidator.class);

        tested = new PinSupplierService(supplierDao, validator);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(supplierDao, validator);
    }

    @Test
    public void test_add() {
        PinSupplier supplier = new PinSupplier();
        supplier.setPinSupplierId(55);
        supplier.setHardwareId(HARDWARE_ID);

        when(supplierDao.addPinSupplier(supplier)).thenReturn(supplier);
        assertNotNull(tested.addPinSupplier(supplier));

        verify(validator).validate(supplier);
        verify(validator).validateHardwareConnected(supplier);
        verify(validator).validateHardwareIdNotUsed(-1, HARDWARE_ID);
        verify(supplierDao).getNextId();

        verify(supplierDao).addPinSupplier(supplier);
    }

    @Test
    public void test_update() {
        PinSupplier pinSupplier = mock(PinSupplier.class);

        PinSupplierUpdate update = new PinSupplierUpdate();
        update.setHardwareId(HARDWARE_ID);
        update.setName(NAME);

        when(supplierDao.getPinSupplier(55)).thenReturn(pinSupplier);
        when(pinSupplier.getPinSupplierId()).thenReturn(55);
        when(pinSupplier.getHardwareId()).thenReturn(HARDWARE_ID);

        when(supplierDao.updatePinSupplier(pinSupplier)).thenReturn(pinSupplier);
        assertNotNull(tested.updatePinSupplier(55, update));

        verify(pinSupplier).setName(NAME);
        verify(pinSupplier).setHardwareId(HARDWARE_ID);
        verify(validator).validate(pinSupplier);
        verify(validator).validateHardwareConnected(pinSupplier);
        verify(validator).validateHardwareIdNotUsed(55, HARDWARE_ID);

        verify(supplierDao).getPinSupplier(55);
        verify(supplierDao).updatePinSupplier(pinSupplier);
    }

    @Test
    public void test_findById() {
        when(supplierDao.getPinSupplier(1)).thenReturn(new PinSupplier());
        assertNotNull(tested.getPinSupplier(1));

        verify(supplierDao).getPinSupplier(1);
    }

    @Test
    public void test_list() {
        when(supplierDao.getPinSuppliers()).thenReturn(new ArrayList<PinSupplier>());
        assertNotNull(tested.listPinSuppliers());

        verify(supplierDao).getPinSuppliers();
    }

    @Test
    public void test_listPins() {
        when(supplierDao.getPins(1)).thenReturn(new ArrayList<Pin>());
        assertNotNull(tested.listPins(1));

        verify(supplierDao).getPins(1);
    }

    @Test
    public void test_delete() {
        tested.deletePinSupplier(1);
        verify(validator).validatePinsNotOwned(1);
        verify(supplierDao).deletePinSupplier(1);
    }

}
