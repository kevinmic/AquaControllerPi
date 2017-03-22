package com.kevin_mic.aqua.service.device;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.service.ErrorType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;

public class DeviceValidatorTest {
    public static final String NAME = "NAME";
    DeviceValidator tested;

    @Before
    public void before() {
        tested = new DeviceValidator();
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

    private Device getValidDevice() {
        Device device = new Device();
        device.setName(NAME);
        device.setType(DeviceType.DosingPumpPeristalticStepper);
        return device;
    }

}