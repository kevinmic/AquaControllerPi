package com.kevin_mic.aqua.types;

import com.kevin_mic.aqua.entity.PinSupplier;

import java.util.ArrayList;
import java.util.List;

public enum PinSupplierType {
    RASBERRY_PI(30),
    PCF8574(8),
    PCF8574A(8),
    SN74HC595(8, true),
    ;

    private int numberOfPins;
    private boolean chained;

    PinSupplierType(int numberOfPins) {
        this(numberOfPins, false);
    }

    PinSupplierType(int numberOfPins, boolean chained) {
        this.numberOfPins = numberOfPins;
        this.chained = chained;
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
}
