package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.DevicePin;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.PinType;
import com.kevin_mic.aqua.service.ErrorType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeviceValidatorTest {
    public static final String NAME = "NAME";
    public static final int DEVICE_ID = 1;
    public static final int PIN_1 = 101;
    public static final int PIN_2 = 102;

    private DeviceValidator tested;
    private PinSupplierDao pinSupplierDao;

    @Before
    public void before() {
        pinSupplierDao = mock(PinSupplierDao.class);
        tested = new DeviceValidator(pinSupplierDao);
    }

    @Test
    public void test_validate_valid() {
        Device device = getValidDevice();
        tested.validate(device);
    }

    @Test
    public void test_validate_invalidName() {
        Device device = getValidDevice();
        device.setName("");

        assertThatThrownBy(() -> tested.validate(device)).hasMessage(ErrorType.DeviceNameRequired.name());
    }

    @Test
    public void test_validate_invalidType() {
        Device device = getValidDevice();
        device.setType(null);

        assertThatThrownBy(() -> tested.validate(device)).hasMessage(ErrorType.DeviceTypeRequired.name());
    }

    @Test
    public void test_validatePinsNotUsed_valid() {
        when(pinSupplierDao.findPin(PIN_1)).thenReturn(new Pin(PIN_1, 1, -1, null));
        when(pinSupplierDao.findPin(PIN_2)).thenReturn(new Pin(PIN_2, 1, -1, DEVICE_ID));

        tested.validatePinsNotUsed(DEVICE_ID, getPins());
    }

    @Test
    public void test_validatePinsNotUsed_invalidOwner() {
        when(pinSupplierDao.findPin(PIN_1)).thenReturn(new Pin(PIN_1, 1, -1, null));
        when(pinSupplierDao.findPin(PIN_2)).thenReturn(new Pin(PIN_2, 1, -1, DEVICE_ID));

        assertThatThrownBy(() -> tested.validatePinsNotUsed(5000, getPins())).hasMessage(ErrorType.PinAlreadyOwned.name());
    }

    @Test
    public void test_validatePinsNotUsed_missingPin() {
        when(pinSupplierDao.findPin(PIN_1)).thenReturn(null);
        when(pinSupplierDao.findPin(PIN_2)).thenReturn(new Pin(PIN_2, 1, -1, DEVICE_ID));

        assertThatThrownBy(() -> tested.validatePinsNotUsed(DEVICE_ID, getPins())).hasMessage(ErrorType.InvalidPinId.name());
    }

    @Test
    public void test_validatePinTypes_valid() {
        tested.validatePinTypes(DeviceType.I2C_BUS, getPins());
    }

    @Test
    public void test_validatePinTypes_missing() {
        assertThatThrownBy(() -> tested.validatePinTypes(DeviceType.I2C_BUS, new ArrayList<>())).hasMessage(ErrorType.WrongNumberOfPins.name());
    }

    @Test
    public void test_validatePinTypes_duplicateType() {
        List<DevicePin> pins = new ArrayList<>();
        pins.add(new DevicePin(0, 0, PinType.I2C_SLC1));
        pins.add(new DevicePin(0, 0, PinType.I2C_SLC1));

        assertThatThrownBy(() -> tested.validatePinTypes(DeviceType.I2C_BUS, pins)).hasMessage(ErrorType.InvalidPinTypes.name());
    }

    @Test
    public void test_validatePinTypes_invalidType() {
        List<DevicePin> pins = new ArrayList<>();
        pins.add(new DevicePin(0, 0, PinType.I2C_SLC1));
        pins.add(new DevicePin(0, 0, PinType.SN74HC595_Clock));

        assertThatThrownBy(() -> tested.validatePinTypes(DeviceType.I2C_BUS, pins)).hasMessage(ErrorType.InvalidPinTypes.name());
    }

    private Device getDevice() {
        Device device = new Device();
        device.setType(DeviceType.I2C_BUS);
        device.setDeviceId(1);
        device.setPins(getPins());
        return device;
    }

    private List<DevicePin> getPins() {
        List<DevicePin> pins = new ArrayList<>();
        pins.add(new DevicePin(PIN_1, DEVICE_ID, PinType.I2C_SDA1));
        pins.add(new DevicePin(PIN_2, DEVICE_ID, PinType.I2C_SLC1));
        return pins;
    }


    private Device getValidDevice() {
        Device device = new Device();
        device.setName(NAME);
        device.setType(DeviceType.DosingPumpPeristalticStepper);
        return device;
    }

}