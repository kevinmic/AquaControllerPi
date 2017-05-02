package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderService;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PinSupplierValidatorTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    public static final String NAME = "NAME";
    public static final int PIN_SUPPLIER_ID = 1;
    PinSupplierValidator tested;
    PinSupplierDao supplierDao;
    private PCF8574ProviderService pcf8574ProviderService;

    @Before
    public void before() {
        supplierDao = mock(PinSupplierDao.class);
        pcf8574ProviderService = mock(PCF8574ProviderService.class);

        tested = new PinSupplierValidator(supplierDao, pcf8574ProviderService);
    }

    @Test
    public void test_validate_valid() {
        PinSupplier pinSupplier = getValidPinSupplier();
        tested.validate(pinSupplier);
        verify(pcf8574ProviderService).assertValidHardwareId(PinSupplierType.PCF8574, HARDWARE_ID);
        Mockito.verifyNoMoreInteractions(supplierDao, pcf8574ProviderService);
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
        pinSupplier.setType(null);

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierTypeCannotBeNull.name());
    }

    @Test
    public void test_validate_invalid_subtype() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setSubType(null);

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierSubTypeCannotBeNull.name());
    }

    @Test
    public void test_validate_invalid_subtypeNotCompatible() {
        PinSupplier pinSupplier = getValidPinSupplier();
        pinSupplier.setSubType(PinSupplierSubType.StepperArray);

        assertThatThrownBy(() -> tested.validate(pinSupplier)).hasMessage(ErrorType.SupplierSubTypeIncompatibleWithType.name());
    }

    @Test
    public void test_validateHardwareidNotUsed_found_invalid() {
        PinSupplier byHardwareId = getValidPinSupplier();
        byHardwareId.setPinSupplierId(55);
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(byHardwareId);
        assertThatThrownBy(() -> tested.validateHardwareIdNotUsed(1, HARDWARE_ID)).hasMessage(ErrorType.SupplierHardwareIdAlreadyUsed.name());
    }

    @Test
    public void test_validateHardwareidNotUsed_found_valid() {
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(getValidPinSupplier());
        tested.validateHardwareIdNotUsed(1, HARDWARE_ID);

        verify(supplierDao).findByHardwareId(HARDWARE_ID);
        Mockito.verifyNoMoreInteractions(supplierDao);
    }

    @Test
    public void test_validateHardwareidNotUsed_notfound_valid() {
        when(supplierDao.findByHardwareId(HARDWARE_ID)).thenReturn(null);
        tested.validateHardwareIdNotUsed(1, HARDWARE_ID);
    }

    @Test
    public void test_validatePinsNotOwned_valid() {
        List<Pin> pins = new ArrayList<>();
        when(supplierDao.getPins(1)).thenReturn(pins);
        tested.validatePinsNotOwned(1);

        verify(supplierDao).getPins(1);
        Mockito.verifyNoMoreInteractions(supplierDao);
    }

    @Test
    public void test_validatePinsNotOwned_invalid() {
        List<Pin> pins = new ArrayList<>();
        pins.add(new Pin(1, 2, 3, 4));
        when(supplierDao.getPins(1)).thenReturn(pins);

        assertThatThrownBy(() -> tested.validatePinsNotOwned(1)).hasMessage(ErrorType.PinsInUse.name());
    }

    @Test
    public void test_validateHardwareNotUsed_isActive() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("" + PCF8574GpioProvider.PCF8574_0x20);
        pinSupplier.setType(PinSupplierType.PCF8574);

        when(pcf8574ProviderService.isDeviceActive(PCF8574GpioProvider.PCF8574_0x20)).thenReturn(true);
        tested.validateHardwareConnected(pinSupplier);

        verify(pcf8574ProviderService).isDeviceActive(PCF8574GpioProvider.PCF8574_0x20);
    }

    @Test
    public void test_validateHardwareNotUsed_A_isActive() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("" + PCF8574GpioProvider.PCF8574A_0x3A);
        pinSupplier.setType(PinSupplierType.PCF8574A);

        when(pcf8574ProviderService.isDeviceActive(PCF8574GpioProvider.PCF8574A_0x3A)).thenReturn(true);
        tested.validateHardwareConnected(pinSupplier);

        verify(pcf8574ProviderService).isDeviceActive(PCF8574GpioProvider.PCF8574A_0x3A);
    }

    @Test
    public void test_validateHardwareNotUsed_isNotActive() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("" + PCF8574GpioProvider.PCF8574_0x20);
        pinSupplier.setType(PinSupplierType.PCF8574);

        when(pcf8574ProviderService.isDeviceActive(PCF8574GpioProvider.PCF8574_0x20)).thenReturn(false);
        assertThatThrownBy(() -> tested.validateHardwareConnected(pinSupplier)).hasMessage(ErrorType.SupplierHardwareIdNotActive.name());

        verify(pcf8574ProviderService).isDeviceActive(PCF8574GpioProvider.PCF8574_0x20);
    }

    @Test
    public void test_validateHardwareNotUsed_notPCF8574() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("notChecked");
        pinSupplier.setType(PinSupplierType.SN74HC595);

        tested.validateHardwareConnected(pinSupplier);
        verifyNoMoreInteractions(pcf8574ProviderService);
    }

    private PinSupplier getValidPinSupplier() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setPinSupplierId(PIN_SUPPLIER_ID);
        pinSupplier.setHardwareId(HARDWARE_ID);
        pinSupplier.setType(PinSupplierType.PCF8574);
        pinSupplier.setSubType(PinSupplierSubType.Relay_12_VDC);
        pinSupplier.setName(NAME);

        return pinSupplier;
    }
}