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
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;

@Slf4j
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
        flipPin(pin, true, "ON");
    }


    public void off(DevicePinSupplierJoin pin) {
        flipPin(pin, false, "ON");
    }

    private void flipPin(DevicePinSupplierJoin pin, boolean doHigh, String debug) {
        if (swapLoHi(pin.getPinSupplierType())) {
            doHigh = !doHigh;
            debug += ":swapLoHigh";
        }

        if (pin.isDeviceDefaultOn()) {
            // Flip the value for defaultOn
            doHigh = !doHigh;
            debug += ":defaultOn";
        }

        GpioPinDigitalOutput gpioPin = getGpioOutputPin(pin);
        if (doHigh) {
            log.info("Pin high - info:{} device:{}, pin:{}, pinSupplierId:{}", debug, pin.getDeviceId(), pin.getPinId(), pin.getPinSupplierId());
            gpioPin.high();
        }
        else {
            log.info("Pin low - info:{} device:{}, pin:{}, pinSupplierId:{}", debug, pin.getDeviceId(), pin.getPinId(), pin.getPinSupplierId());
            gpioPin.low();
        }
    }

    private boolean swapLoHi(PinSupplierType pinSupplierType) {
        // For PCF8574 we are going to sink current, which mans LOW = HIGH
        return pinSupplierType == PinSupplierType.PCF8574 || pinSupplierType== PinSupplierType.PCF8574A;
    }


    private GpioPinDigitalOutput provisionOutputPin(DevicePinSupplierJoin pin) {
        return PinLock.lock(() -> {
            GpioPinDigitalOutput gpioPin = (GpioPinDigitalOutput) gpioController.getProvisionedPin("pin_" + pin.getPinId());
            if (gpioPin != null) {
                return gpioPin;
            }

            switch (pin.getPinSupplierType()) {
                case RASBERRY_PI:
                    log.info("PROVISION PI PIN - device:{}, pin:{}, pinSupplierId:{}", pin.getDeviceId(), pin.getPinId(), pin.getPinSupplierId());
                    return gpioController.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin.getPinNumber()), getPinName(pin.getPinId()), PinState.LOW);
                case PCF8574:
                case PCF8574A:
                    log.info("PROVISION PCF8574 PIN - device:{}, pin:{}, pinSupplierId:{}", pin.getDeviceId(), pin.getPinId(), pin.getPinSupplierId());
                    return gpioController.provisionDigitalOutputPin(pcf8574ProviderService.getProvider(pin.getPinSupplierType(), pin.getPinSupplierHardwareId()), getPcfPin(pin.getPinNumber()), getPinName(pin.getPinId()), PinState.HIGH);
                default:
                    throw new RuntimeException("PinSupplierType Not Handled: " + pin.getPinSupplierType());
            }
        });
    }

    public void unProvisionPins(List<Integer> pins) {
        pins.forEach(pin -> {
            log.info("PROVISION PI PIN - pin:{}", pin);
            GpioPin provisionedPin = gpioController.getProvisionedPin(getPinName(pin));
            if (provisionedPin != null) {
                gpioController.unprovisionPin(provisionedPin);
            }
        });
    }
}
