package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        pinSupplier.setHardwareId("");

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierHardwareIdCannotBeNull.name());
    }

    @Test
    public void test_validate_invalid_name() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setName("");

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierNameCannotBeNull.name());
    }

    @Test
    public void test_validate_invalid_type() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setSupplierType(null);

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierTypeCannotBeNull.name());
    }

    @Test
    public void test_validateHardwareidNotUsed_found() {
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(getValidPinSupplier());
        assertThatThrownBy(() -> tested.validateHardwareIdNotUsed(getValidPinSupplier())).hasMessage(ErrorType.SupplierHardwareIdAlreadyUsed.name());
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