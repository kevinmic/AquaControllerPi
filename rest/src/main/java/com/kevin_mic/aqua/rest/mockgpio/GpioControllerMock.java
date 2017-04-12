package com.kevin_mic.aqua.rest.mockgpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalog;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;

public class GpioControllerMock implements GpioController {
    private boolean shutdownFlag = false;

    @Override
    public void export(PinMode mode, PinState defaultState, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void export(PinMode mode, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isExported(GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void unexport(Pin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void unexport(GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void unexportAll() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setMode(PinMode mode, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public PinMode getMode(GpioPin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isMode(PinMode mode, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setPullResistance(PinPullResistance resistance, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public PinPullResistance getPullResistance(GpioPin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance, GpioPin... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void high(GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isHigh(GpioPinDigital... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void low(GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isLow(GpioPinDigital... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setState(PinState state, GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setState(boolean state, GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isState(PinState state, GpioPinDigital... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public PinState getState(GpioPinDigital pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void toggle(GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void pulse(long milliseconds, GpioPinDigitalOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setValue(double value, GpioPinAnalogOutput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public double getValue(GpioPinAnalog pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addListener(GpioPinListener listener, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addListener(GpioPinListener[] listeners, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeListener(GpioPinListener listener, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeListener(GpioPinListener[] listeners, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeAllListeners() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addTrigger(GpioTrigger trigger, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeTrigger(GpioTrigger trigger, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeAllTriggers() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name, double defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, double defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name, double defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(GpioProvider provider, Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, String name, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, int defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin, String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPinPwmOutput provisionSoftPwmOutputPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin provisionPin(Pin pin, String name, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin provisionPin(Pin pin, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options, GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode, GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Collection<GpioPin> getProvisionedPins() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin getProvisionedPin(Pin pin) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public GpioPin getProvisionedPin(String name) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void unprovisionPin(GpioPin... pin) {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isShutdown() {
        return shutdownFlag;
    }

    @Override
    public void shutdown() {
        shutdownFlag = true;
    }
}
