package com.kevin_mic.aqua.service.gpio;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;

public interface PCF8574ProviderFactory {
    GpioProvider createProvider(int pinSupplierHardwareId);

    I2CBus getBus(int busNumber);
}
