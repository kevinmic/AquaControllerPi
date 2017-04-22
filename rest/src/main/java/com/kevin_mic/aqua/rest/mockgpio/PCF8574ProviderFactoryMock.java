package com.kevin_mic.aqua.rest.mockgpio;

import com.kevin_mic.aqua.service.gpio.PCF8574ProviderFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinListener;

public class PCF8574ProviderFactoryMock implements PCF8574ProviderFactory {
    @Override
    public GpioProvider createProvider(int pinSupplierHardwareId) {
        return new GpioProvider() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean hasPin(Pin pin) {
                return false;
            }

            @Override
            public void export(Pin pin, PinMode mode, PinState defaultState) {

            }

            @Override
            public void export(Pin pin, PinMode mode) {

            }

            @Override
            public boolean isExported(Pin pin) {
                return false;
            }

            @Override
            public void unexport(Pin pin) {

            }

            @Override
            public void setMode(Pin pin, PinMode mode) {

            }

            @Override
            public PinMode getMode(Pin pin) {
                return null;
            }

            @Override
            public void setPullResistance(Pin pin, PinPullResistance resistance) {

            }

            @Override
            public PinPullResistance getPullResistance(Pin pin) {
                return null;
            }

            @Override
            public void setState(Pin pin, PinState state) {

            }

            @Override
            public PinState getState(Pin pin) {
                return null;
            }

            @Override
            public void setValue(Pin pin, double value) {

            }

            @Override
            public double getValue(Pin pin) {
                return 0;
            }

            @Override
            public void setPwm(Pin pin, int value) {

            }

            @Override
            public void setPwmRange(Pin pin, int range) {

            }

            @Override
            public int getPwm(Pin pin) {
                return 0;
            }

            @Override
            public void addListener(Pin pin, PinListener listener) {

            }

            @Override
            public void removeListener(Pin pin, PinListener listener) {

            }

            @Override
            public void removeAllListeners() {

            }

            @Override
            public void shutdown() {

            }

            @Override
            public boolean isShutdown() {
                return false;
            }
        };
    }
}
