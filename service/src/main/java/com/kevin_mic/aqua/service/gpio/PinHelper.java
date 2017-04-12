package com.kevin_mic.aqua.service.gpio;

import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class PinHelper {
    public static void on(PinSupplierType type, PinSupplierSubType subType, GpioPinDigitalOutput provisionedPin) {
        if (type == PinSupplierType.PCF8574 || type == PinSupplierType.PCF8574A) {
            // For PCF8574 we are going to sink current, which mans LOW = HIGH
            provisionedPin.low();
        }
        else {
            provisionedPin.high();
        }
    }

    public static void off(PinSupplierType type, PinSupplierSubType subType, GpioPinDigitalOutput provisionedPin) {
        if (type == PinSupplierType.PCF8574 || type == PinSupplierType.PCF8574A) {
            // For PCF8574 we are going to sink current, which mans HIGH = LOW
            provisionedPin.high();
        }
        else {
            provisionedPin.low();
        }
    }
}
