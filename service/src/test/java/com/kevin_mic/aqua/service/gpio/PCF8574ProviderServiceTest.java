package com.kevin_mic.aqua.service.gpio;

import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
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
}
