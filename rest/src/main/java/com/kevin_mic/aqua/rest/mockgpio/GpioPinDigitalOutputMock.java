package com.kevin_mic.aqua.rest.mockgpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
public class GpioPinDigitalOutputMock implements GpioPinDigitalOutput {
    private String name = null;
    private Pin pin = null;

    public GpioPinDigitalOutputMock(String name, Pin pin) {
        this.name = name;
        this.pin = pin;
    }

    @Override
    public void high() {
        log.info("pin:{} - high", name);
    }

    @Override
    public void low() {
        log.info("pin:{} - low", name);
    }

    @Override
    public void toggle() {
        log.info("pin:{} - toggle", name);
    }

    @Override
    public Future<?> blink(long delay) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> blink(long delay, PinState blinkState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> blink(long delay, long duration) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> blink(long delay, long duration, PinState blinkState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, Callable<Void> callback) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, boolean blocking) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, boolean blocking, Callable<Void> callback) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, boolean blocking) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setState(PinState state) {
        log.info("pin:{} - setState:{}", name, state);
    }

    @Override
    public void setState(boolean state) {
        log.info("pin:{} - setState:{}", name, state);
    }

    @Override
    public boolean isHigh() {
        log.info("pin:{} - isHigh", name);
        return true;
    }

    @Override
    public boolean isLow() {
        log.info("pin:{} - isLow", name);
        return false;
    }

    @Override
    public PinState getState() {
        log.info("pin:{} - getState", name);
        return PinState.HIGH;
    }

    @Override
    public boolean isState(PinState state) {
        log.info("pin:{} - isState:", name, state);
        return state == PinState.HIGH;
    }

    @Override
    public GpioProvider getProvider() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Pin getPin() {
        return pin;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setTag(Object tag) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Object getTag() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setProperty(String key, String value) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean hasProperty(String key) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public String getProperty(String key) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Map<String, String> getProperties() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeProperty(String key) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void clearProperties() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void export(PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void export(PinMode mode, PinState defaultState) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void unexport() {

        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isExported() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setMode(PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public PinMode getMode() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isMode(PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setPullResistance(PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public PinPullResistance getPullResistance() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Collection<GpioPinListener> getListeners() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addListener(GpioPinListener... listener) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void addListener(List<? extends GpioPinListener> listeners) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public boolean hasListener(GpioPinListener... listener) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void removeListener(GpioPinListener... listener) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void removeListener(List<? extends GpioPinListener> listeners) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void removeAllListeners() {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public GpioPinShutdown getShutdownOptions() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void setShutdownOptions(Boolean unexport) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode) {
        throw new RuntimeException("NOT IMPLEMENTED");

    }
}
