package com.kevin_mic.aqua.service.gpio;

import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PCF8574ProviderFactory {
    private final Map<String, PCF8574GpioProvider> provisionedProviders = new HashMap<>();

    public GpioProvider getProvider(PinSupplierType pinSupplierType, String pinSupplierHardwareId) {
        PCF8574GpioProvider provider = provisionedProviders.get(pinSupplierType.name() + "_" + pinSupplierHardwareId);
        if (provider == null) {
            provider = createProvider(pinSupplierType, pinSupplierHardwareId);
        }

        return provider;
    }

    private PCF8574GpioProvider createProvider(PinSupplierType pinSupplierType, String pinSupplierHardwareId) {
        return PinLock.lockSupplier(() -> {
            PCF8574GpioProvider provider = provisionedProviders.get(pinSupplierType.name() + "_" + pinSupplierHardwareId);
            if (provider == null) {
                try {
                    provider = new PCF8574GpioProvider(I2CBus.BUS_1, Integer.parseInt(pinSupplierHardwareId));
                    provisionedProviders.put(pinSupplierHardwareId, provider);
                } catch (I2CFactory.UnsupportedBusNumberException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return provider;
        });
    }
}
