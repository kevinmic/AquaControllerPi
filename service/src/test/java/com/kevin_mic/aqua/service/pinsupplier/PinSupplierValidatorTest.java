package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.entity.PinSupplier;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.types.PinSupplierType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PinSupplierValidatorTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    public static final String NAME = "NAME";
    PinSupplierValidator tested;
    PinSupplierDao supplierDao;

    @Before
    public void before() {
        supplierDao = mock(PinSupplierDao.class);

        tested = new PinSupplierValidator(supplierDao);
    }

    @Test
    public void test_validate_valid() {
        PinSupplier pinSupplier = getValidPinSupplier();
        tested.validate(pinSupplier);
    }

    @Test
    public void test_validate_invalid_Hardware() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setHardwareId(null);
        try {
            tested.validate(pinSupplier);
            fail();
        }
        catch (AquaException e) {
            assertEquals(ErrorType.SupplierHardwareIdCannotBeNull, e.getErrorType());
        }
    }

    @Test
    public void test_validate_invalid_name() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setName(null);
        try {
            tested.validate(pinSupplier);
            fail();
        }
        catch (AquaException e) {
            assertEquals(ErrorType.SupplierNameCannotBeNull, e.getErrorType());
        }
    }

    @Test
    public void test_validate_invalid_type() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setSupplierType(null);
        try {
            tested.validate(pinSupplier);
            fail();
        }
        catch (AquaException e) {
            assertEquals(ErrorType.SupplierTypeCannotBeNull, e.getErrorType());
        }
    }

    @Test
    public void test_validateHardwareidNotUsed_found() {
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(getValidPinSupplier());
        try {
            tested.validateHardwareIdNotUsed(getValidPinSupplier());
            fail();
        }
        catch (AquaException e) {
            assertEquals(ErrorType.SupplierHardwareIdAlreadyUsed, e.getErrorType());
        }
    }

    @Test
    public void test_validateHardwareidNotUsed_notfound() {
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(null);
        tested.validateHardwareIdNotUsed(getValidPinSupplier());
    }

    private PinSupplier getValidPinSupplier() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId(HARDWARE_ID);
        pinSupplier.setSupplierType(PinSupplierType.PCF8574);
        pinSupplier.setName(NAME);

        return pinSupplier;
    }
}