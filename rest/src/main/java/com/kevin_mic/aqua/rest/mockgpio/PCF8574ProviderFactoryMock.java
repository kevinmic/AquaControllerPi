package com.kevin_mic.aqua.rest.mockgpio;

import com.kevin_mic.aqua.service.gpio.PCF8574ProviderFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

import java.io.IOException;

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

    @Override
    public I2CBus getBus(int busNumber) {
        return new I2CBus() {
            @Override
            public I2CDevice getDevice(int address) throws IOException {
                return new I2CDevice() {
                    @Override
                    public int getAddress() {
                        return address;
                    }

                    @Override
                    public void write(byte b) throws IOException {

                    }

                    @Override
                    public void write(byte[] buffer, int offset, int size) throws IOException {

                    }

                    @Override
                    public void write(byte[] buffer) throws IOException {

                    }

                    @Override
                    public void write(int address, byte b) throws IOException {

                    }

                    @Override
                    public void write(int address, byte[] buffer, int offset, int size) throws IOException {

                    }

                    @Override
                    public void write(int address, byte[] buffer) throws IOException {

                    }

                    @Override
                    public int read() throws IOException {
                        return 0;
                    }

                    @Override
                    public int read(byte[] buffer, int offset, int size) throws IOException {
                        return 0;
                    }

                    @Override
                    public int read(int address) throws IOException {
                        return 0;
                    }

                    @Override
                    public int read(int address, byte[] buffer, int offset, int size) throws IOException {
                        return 0;
                    }

                    @Override
                    public int read(byte[] writeBuffer, int writeOffset, int writeSize, byte[] readBuffer, int readOffset, int readSize) throws IOException {
                        return 0;
                    }
                };
            }

            @Override
            public int getBusNumber() {
                return busNumber;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }
}
