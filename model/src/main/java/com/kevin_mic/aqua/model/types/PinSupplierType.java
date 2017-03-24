package com.kevin_mic.aqua.model.types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum PinSupplierType {
    RASBERRY_PI(30, PinSupplierSubType.SensorArray),
    PCF8574(8, PinSupplierSubType.Relay_12_VDC, PinSupplierSubType.Relay_120_VAC, PinSupplierSubType.SensorArray),
    PCF8574A(8, PinSupplierSubType.Relay_12_VDC, PinSupplierSubType.Relay_120_VAC, PinSupplierSubType.SensorArray),
    SN74HC595(8, true, PinSupplierSubType.StepperArray),
    ;

    private int numberOfPins;
    private boolean chained;
    private Set<PinSupplierSubType> allowedSubTypes;

    PinSupplierType(int numberOfPins, PinSupplierSubType ... allowedSubTypes) {
        this(numberOfPins, false, allowedSubTypes);
    }

    PinSupplierType(int numberOfPins, boolean chained, PinSupplierSubType ... allowedSubTypes) {
        this.numberOfPins = numberOfPins;
        this.chained = chained;

        this.allowedSubTypes = new HashSet<>();
        this.allowedSubTypes.addAll(Arrays.asList(allowedSubTypes));
    }

    public int convertPinIdToPinNumber(String hardwareId, int pinNumber) {

        if (chained) {
            int chainNumber = Integer.parseInt(hardwareId);
            pinNumber = (numberOfPins * (chainNumber - 1)) + pinNumber;
        }
        return pinNumber;
    }

    public int getNumberOfPins() {
        return numberOfPins;
    }

    public boolean isSubTypeAllowed(PinSupplierSubType subType) {
        return allowedSubTypes.contains(subType);
    }
}
