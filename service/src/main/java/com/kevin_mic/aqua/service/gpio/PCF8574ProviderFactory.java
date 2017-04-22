package com.kevin_mic.aqua.service.gpio;

import com.pi4j.io.gpio.GpioProvider;

public interface PCF8574ProviderFactory {
    GpioProvider createProvider(int pinSupplierHardwareId);
}
