package com.kevin_mic.aqua.service.gpio;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class PCF8574ProviderFactoryImpl implements PCF8574ProviderFactory {
    @Override
    public GpioProvider createProvider(int pinSupplierHardwareId) {
        try {
            return new PCF8574GpioProvider(I2CBus.BUS_1, pinSupplierHardwareId);
        } catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public I2CBus getBus(int busNum) {
        try {
            return I2CFactory.getInstance(busNum);
        } catch (I2CFactory.UnsupportedBusNumberException|IOException  e) {
            throw new RuntimeException(e);
        }
    }
}
