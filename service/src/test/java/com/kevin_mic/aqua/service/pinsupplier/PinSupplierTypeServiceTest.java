package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.PinSupplier;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PinSupplierTypeServiceTest {
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
        tested.add(supplier);

        verify(validator).validate(supplier);
        verify(validator).validateHardwareConnected(supplier);
        verify(validator).validateHardwareIdNotUsed(supplier);

        verify(supplierDao).insertSupplier(supplier);
    }

}
