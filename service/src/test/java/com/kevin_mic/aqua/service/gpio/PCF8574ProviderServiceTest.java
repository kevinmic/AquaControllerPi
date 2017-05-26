package com.kevin_mic.aqua.service.gpio;
import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.ErrorType;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PCF8574ProviderServiceTest {
    PCF8574ProviderService tested;

    PCF8574ProviderFactoryImpl factory;

    @Before
    public void before() {
        factory = mock(PCF8574ProviderFactoryImpl.class);
        tested = new PCF8574ProviderService(factory);
    }

    @Test
    public void test_create_shutdown_provider() {
        PCF8574GpioProvider provider = mock(PCF8574GpioProvider.class);
        when(factory.createProvider(1)).thenReturn(provider);
        assertEquals(provider, tested.getProvider(PinSupplierType.PCF8574, "1"));

        tested.shutdownBus(PinSupplierType.PCF8574, "1");
        verify(provider).shutdown();
    }

    @Test
    public void test_lookupActiveNonExistingProviders() throws IOException {
        I2CBus bus = mock(I2CBus.class);
        I2CDevice device = mock(I2CDevice.class);

        when(factory.getBus(I2CBus.BUS_1)).thenReturn(bus);
        when(bus.getDevice(anyInt())).thenReturn(device);

        List<Integer> found = tested.lookupActiveNonExistingProviders(PinSupplierType.PCF8574, Lists.newArrayList(PCF8574GpioProvider.PCF8574_0x20, PCF8574GpioProvider.PCF8574_0x22, PCF8574GpioProvider.PCF8574_0x24, PCF8574GpioProvider.PCF8574_0x26));
        assertEquals(4, found.size());
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574_0x21));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574_0x23));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574_0x25));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574_0x27));
    }

    @Test
    public void test_lookupActiveNonExistingProviders_A() throws IOException {
        I2CBus bus = mock(I2CBus.class);
        I2CDevice device = mock(I2CDevice.class);

        when(factory.getBus(I2CBus.BUS_1)).thenReturn(bus);
        when(bus.getDevice(anyInt())).thenReturn(device);

        List<Integer> found = tested.lookupActiveNonExistingProviders(PinSupplierType.PCF8574A, Lists.newArrayList(PCF8574GpioProvider.PCF8574A_0x38, PCF8574GpioProvider.PCF8574A_0x3A, PCF8574GpioProvider.PCF8574A_0x3C, PCF8574GpioProvider.PCF8574A_0x3E));
        assertEquals(4, found.size());
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574A_0x39));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574A_0x3B));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574A_0x3D));
        assertTrue(found.contains(PCF8574GpioProvider.PCF8574A_0x3F));
    }

    @Test
    public void test_active() throws IOException {
        I2CBus bus = mock(I2CBus.class);
        I2CDevice device = mock(I2CDevice.class);
        int deviceId = PCF8574GpioProvider.PCF8574_0x20;

        when(factory.getBus(I2CBus.BUS_1)).thenReturn(bus);
        when(bus.getDevice(deviceId)).thenReturn(device);

        assertTrue(tested.isDeviceActive(deviceId));

        verify(device).read(any(), anyInt(), anyInt());

    }

    @Test
    public void test_inactive() throws IOException {
        I2CBus bus = mock(I2CBus.class);
        I2CDevice device = mock(I2CDevice.class);
        int deviceId = PCF8574GpioProvider.PCF8574_0x20;

        when(factory.getBus(I2CBus.BUS_1)).thenReturn(bus);
        when(bus.getDevice(deviceId)).thenReturn(device);
        when(device.read(any(), anyInt(), anyInt())).thenThrow(new IOException());

        assertFalse(tested.isDeviceActive(deviceId));

        verify(device).read(any(), anyInt(), anyInt());

    }

    @Test
    public void test_assertValidHardwareId_valid() throws IOException {
        tested.assertValidHardwareId(PinSupplierType.PCF8574, "" + PCF8574GpioProvider.PCF8574_0x20);
    }

    @Test
    public void test_assertValidHardwareId_invalid() throws IOException {
         assertThatThrownBy(() -> tested.assertValidHardwareId(PinSupplierType.PCF8574, "" + PCF8574GpioProvider.PCF8574A_0x38)).hasMessage(ErrorType.InvalidHardwareId_PCF8574_BadAddress.name());
    }

    @Test
    public void test_assertValidHardwareId_valid_A() throws IOException {
        tested.assertValidHardwareId(PinSupplierType.PCF8574A, "" + PCF8574GpioProvider.PCF8574A_0x38);
    }

    @Test
    public void test_assertValidHardwareId_invalid_A() throws IOException {
        assertThatThrownBy(() -> tested.assertValidHardwareId(PinSupplierType.PCF8574A, "" + PCF8574GpioProvider.PCF8574_0x20)).hasMessage(ErrorType.InvalidHardwareId_PCF8574_BadAddress.name());
    }
}
