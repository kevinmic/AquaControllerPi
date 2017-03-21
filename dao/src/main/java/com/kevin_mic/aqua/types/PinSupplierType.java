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

    // TODO Write Tests
    public List<String> getPinIds(int supplierId, String hardwareId) {
        List<String> pinIds = new ArrayList<>();
        for (int i = 0; i < numberOfPins; i++) {
            pinIds.add(supplierId + ":" + i);
        }
        return pinIds;
    }

    // TODO Write Tests
    public int convertPinIdToPinNumber(String hardwareId, String pinId) {
        int pin = Integer.parseInt(pinId.substring(pinId.indexOf(":") + 1));

        if (chained) {
            int chainNumber = Integer.parseInt(hardwareId);
            pin = (numberOfPins * (chainNumber - 1)) + pin;
        }
        return pin;
    }
}
