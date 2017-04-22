package com.kevin_mic.aqua.service.gpio;

import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PCF8574ProviderService {
    private final PCF8574ProviderFactory pcf8574ProviderFactory;

    private final Map<String, GpioProvider> provisionedProviders = new HashMap<>();

    @Inject
    public PCF8574ProviderService(PCF8574ProviderFactory pcf8574ProviderFactory) {
        this.pcf8574ProviderFactory = pcf8574ProviderFactory;
    }


    public GpioProvider getProvider(PinSupplierType pinSupplierType, String pinSupplierHardwareId) {
        GpioProvider provider = provisionedProviders.get(getKey(pinSupplierType, pinSupplierHardwareId));
        if (provider == null) {
            provider = createProvider(pinSupplierType, pinSupplierHardwareId);
        }

        return provider;
    }

    private GpioProvider createProvider(PinSupplierType pinSupplierType, String hardwareId) {
        return PinLock.lock(() -> {
            GpioProvider provider = provisionedProviders.get(getKey(pinSupplierType, hardwareId));
            if (provider == null) {
                provider = pcf8574ProviderFactory.createProvider(Integer.parseInt(hardwareId));
                provisionedProviders.put(getKey(pinSupplierType, hardwareId), provider);
            }
            return provider;
        });
    }

    private String getKey(PinSupplierType pinSupplierType, String pinSupplierHardwareId) {
        return pinSupplierType.name() + "_" + pinSupplierHardwareId;
    }

    public void shutdownBus(PinSupplierType type, String hardwareId) {
        GpioProvider provider = provisionedProviders.remove(getKey(type, hardwareId));
        if (provider != null) {
            provider.shutdown();
        }
    }
}
