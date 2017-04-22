package com.kevin_mic.aqua.service.gpio;

import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import javax.inject.Inject;
import java.util.List;

public class PinController {
    private final GpioController gpioController;
    private final PCF8574ProviderService pcf8574ProviderService;

    @Inject
    public PinController(GpioController gpioController, PCF8574ProviderService pcf8574ProviderService) {
        this.gpioController = gpioController;
        this.pcf8574ProviderService = pcf8574ProviderService;
    }

    public GpioPinDigitalOutput getGpioOutputPin(DevicePinSupplierJoin pin) {
        GpioPinDigitalOutput gpioPin = (GpioPinDigitalOutput) gpioController.getProvisionedPin("pin_" + pin.getPinId());
        if (gpioPin == null) {
            gpioPin = provisionOutputPin(pin);
        }
        return gpioPin;
    }

    private Pin getPcfPin(int pinNumber) {
        for (Pin pin : PCF8574Pin.ALL) {
            if (pin.getAddress() == pinNumber) {
                return pin;
            }
        }
        throw new RuntimeException("INVALID PCF PIN NUMBER " + pinNumber);
    }

    private String getPinName(int pinId) {
        return "pin_" + pinId;
    }

    public void on(DevicePinSupplierJoin pin) {
        GpioPinDigitalOutput gpioPin = getGpioOutputPin(pin);
        if (pin.getPinSupplierType() == PinSupplierType.PCF8574 || pin.getPinSupplierType() == PinSupplierType.PCF8574A) {
            // For PCF8574 we are going to sink current, which mans LOW = HIGH
            gpioPin.low();
        }
        else {
            gpioPin.high();
        }
    }

    public void off(DevicePinSupplierJoin pin) {
        GpioPinDigitalOutput gpioPin = getGpioOutputPin(pin);
        if (pin.getPinSupplierType() == PinSupplierType.PCF8574 || pin.getPinSupplierType() == PinSupplierType.PCF8574A) {
            // For PCF8574 we are going to sink current, which mans HIGH = LOW
            gpioPin.high();
        }
        else {
            gpioPin.low();
        }
    }

    private GpioPinDigitalOutput provisionOutputPin(DevicePinSupplierJoin pin) {
        return PinLock.lock(() -> {
            GpioPinDigitalOutput gpioPin = (GpioPinDigitalOutput) gpioController.getProvisionedPin("pin_" + pin.getPinId());
            if (gpioPin != null) {
                return gpioPin;
            }

            switch (pin.getPinSupplierType()) {
                case RASBERRY_PI:
                    return gpioController.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin.getPinNumber()), getPinName(pin.getPinId()), PinState.LOW);
                case PCF8574:
                case PCF8574A:
                    return gpioController.provisionDigitalOutputPin(pcf8574ProviderService.getProvider(pin.getPinSupplierType(), pin.getPinSupplierHardwareId()), getPcfPin(pin.getPinNumber()), getPinName(pin.getPinId()), PinState.HIGH);
                default:
                    throw new RuntimeException("PinSupplierType Not Handled: " + pin.getPinSupplierType());
            }
        });
    }

    public void unProvisionPins(List<Integer> pins) {
        pins.forEach(pin -> {
            GpioPin provisionedPin = gpioController.getProvisionedPin(getPinName(pin));
            if (provisionedPin != null) {
                gpioController.unprovisionPin(provisionedPin);
            }
        });
    }
}
