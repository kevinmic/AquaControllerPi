package com.kevin_mic.aqua.service.gpio;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class PCF8574ProviderService {
    private static final List<Integer> PCF8574_HWIDS = Lists.newArrayList(PCF8574GpioProvider.PCF8574_0x20, PCF8574GpioProvider.PCF8574_0x21, PCF8574GpioProvider.PCF8574_0x22, PCF8574GpioProvider.PCF8574_0x23, PCF8574GpioProvider.PCF8574_0x24, PCF8574GpioProvider.PCF8574_0x25, PCF8574GpioProvider.PCF8574_0x26, PCF8574GpioProvider.PCF8574_0x27);
    private static final List<Integer> PCF8574A_HWIDS = Lists.newArrayList(PCF8574GpioProvider.PCF8574A_0x38, PCF8574GpioProvider.PCF8574A_0x39, PCF8574GpioProvider.PCF8574A_0x3A, PCF8574GpioProvider.PCF8574A_0x3B, PCF8574GpioProvider.PCF8574A_0x3C, PCF8574GpioProvider.PCF8574A_0x3D, PCF8574GpioProvider.PCF8574A_0x3E, PCF8574GpioProvider.PCF8574A_0x3F);

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

    public List<Integer> lookupActiveNonExistingProviders(PinSupplierType supplierType, List<Integer> existingIds) {
        List<Integer> possibleAddresses = getPossibleAddresses(supplierType);

        possibleAddresses.removeAll(existingIds);


        I2CBus bus = null;
        try {
            bus = pcf8574ProviderFactory.getBus(I2CBus.BUS_2);
            final I2CBus finalBus = bus;
            return possibleAddresses.stream().filter((hwId) -> isDeviceActive(finalBus, hwId)).collect(Collectors.toList());
        } finally {
            if (bus != null) {
                try {
                    bus.close();
                } catch (IOException e1) {
                    log.error("Error closing bus", e1);
                }
            }
        }
    }

    private List<Integer> getPossibleAddresses(PinSupplierType supplierType) {
        switch (supplierType) {
            case PCF8574:
                return new ArrayList<>(PCF8574_HWIDS);
            case PCF8574A:
                return new ArrayList<>(PCF8574A_HWIDS);
            default:
                throw new AquaException(ErrorType.InvalidSupplierType);
        }
    }

    public boolean isDeviceActive(Integer hwId) {
        if (provisionedProviders.containsKey(hwId)) {
            return true;
        }

        I2CBus bus = null;
        try {
            bus = pcf8574ProviderFactory.getBus(I2CBus.BUS_2);
            return isDeviceActive(bus, hwId);
        } finally {
            if (bus != null) {
                try {
                    bus.close();
                } catch (IOException e1) {
                    log.error("Error closing bus", e1);
                }
            }
        }
    }

    private boolean isDeviceActive(I2CBus bus, Integer hwId) {
        try {
            I2CDevice device = bus.getDevice(hwId);

            byte[] buffer = new byte[1];
            device.read(buffer, 0, 1);

            // We will assume if there is no exception so far, that it is a valid device
            return true;
        }
        catch (Exception e) {
            // We will assume if there is an exception, then this address is not active.
            return false;
        }
    }

    public void assertValidHardwareId(PinSupplierType type, String hardwareId) {
        try {
            int address = Integer.parseInt(hardwareId);

            if (type != PinSupplierType.PCF8574 && type != PinSupplierType.PCF8574A) {
                throw new RuntimeException("ERROR CALLING assertValidHardwareId with invalid type");
            }

            if (!getPossibleAddresses(type).contains(address)) {
                throw new AquaException(ErrorType.InvalidHardwareId_PCF8574_BadAddress);
            }
        }
        catch (NumberFormatException e) {
            throw new AquaException(ErrorType.InvalidHardwareId_PCF8574_MustBeInt);
        }
    }
}
