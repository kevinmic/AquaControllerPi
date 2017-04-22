package com.kevin_mic.aqua.service.gpio;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PinControllerTest {
    PinController tested;
    GpioController controller;
    PCF8574ProviderService pcf8574ProviderService;

    GpioPinDigitalOutput outputPin;

    @Before
    public void before() {
        controller = mock(GpioController.class);
        pcf8574ProviderService = mock(PCF8574ProviderService.class);
        outputPin = mock(GpioPinDigitalOutput.class);

        tested = new PinController(controller, pcf8574ProviderService);
    }

    @Test
    public void getPin_alreadyProvisioned() {
        DevicePinSupplierJoin pin = getPin();

        when(controller.getProvisionedPin("pin_1")).thenReturn(outputPin);
        assertEquals(outputPin, tested.getGpioOutputPin(pin));

        verify(controller, times(1)).getProvisionedPin("pin_1");
    }

    @Test
    public void getPin_notProvisioned_pi() {
        DevicePinSupplierJoin pin = getPin();

        when(controller.getProvisionedPin("pin_1")).thenReturn(null);
        when(controller.provisionDigitalOutputPin(any(Pin.class), anyString(), any(PinState.class))).thenReturn(outputPin);
        GpioPinDigitalOutput gpioOutputPin = tested.getGpioOutputPin(pin);

        verify(controller, times(2)).getProvisionedPin("pin_1");
        verify(controller, times(1)).provisionDigitalOutputPin(RaspiPin.GPIO_00, "pin_1", PinState.LOW);
        assertEquals(outputPin, gpioOutputPin);
    }

    @Test
    public void getPin_notProvisioned_pcf8574() {
        DevicePinSupplierJoin pin = getPin(1, PinSupplierType.PCF8574, "5");

        GpioProvider provider = mock(GpioProvider.class);

        when(controller.getProvisionedPin("pin_1")).thenReturn(null);
        when(pcf8574ProviderService.getProvider(any(PinSupplierType.class), anyString())).thenReturn(provider);
        when(controller.provisionDigitalOutputPin(any(GpioProvider.class), any(Pin.class), anyString(), any(PinState.class))).thenReturn(outputPin);
        GpioPinDigitalOutput gpioOutputPin = tested.getGpioOutputPin(pin);

        verify(controller, times(2)).getProvisionedPin("pin_1");
        verify(controller, times(1)).provisionDigitalOutputPin(provider, PCF8574Pin.GPIO_00, "pin_1", PinState.HIGH);
        verify(pcf8574ProviderService, times(1)).getProvider(PinSupplierType.PCF8574, "5");
        assertEquals(outputPin, gpioOutputPin);
    }

    @Test
    public void getPin_notProvisioned_pcf8574A() {
        DevicePinSupplierJoin pin = getPin(1, PinSupplierType.PCF8574A, "5");

        GpioProvider provider = mock(GpioProvider.class);

        when(controller.getProvisionedPin("pin_1")).thenReturn(null);
        when(pcf8574ProviderService.getProvider(any(PinSupplierType.class), anyString())).thenReturn(provider);
        when(controller.provisionDigitalOutputPin(any(GpioProvider.class), any(Pin.class), anyString(), any(PinState.class))).thenReturn(outputPin);
        GpioPinDigitalOutput gpioOutputPin = tested.getGpioOutputPin(pin);

        verify(controller, times(2)).getProvisionedPin("pin_1");
        verify(controller, times(1)).provisionDigitalOutputPin(provider, PCF8574Pin.GPIO_00, "pin_1", PinState.HIGH);
        verify(pcf8574ProviderService, times(1)).getProvider(PinSupplierType.PCF8574A, "5");
        assertEquals(outputPin, gpioOutputPin);
    }

    @Test
    public void test_on_raspi() {
        DevicePinSupplierJoin pin = getPin();

        when(controller.getProvisionedPin("pin_1")).thenReturn(outputPin);
        tested.on(pin);

        verify(controller, times(1)).getProvisionedPin("pin_1");
        verify(outputPin).high();
    }

    @Test
    public void test_off_raspi() {
        DevicePinSupplierJoin pin = getPin();

        when(controller.getProvisionedPin("pin_1")).thenReturn(outputPin);
        tested.off(pin);

        verify(controller, times(1)).getProvisionedPin("pin_1");
        verify(outputPin).low();
    }

    @Test
    public void test_on_pcf8574() {
        DevicePinSupplierJoin pin1 = getPin(1, PinSupplierType.PCF8574, "");
        DevicePinSupplierJoin pin2 = getPin(2, PinSupplierType.PCF8574A, "");

        when(controller.getProvisionedPin("pin_1")).thenReturn(outputPin);
        when(controller.getProvisionedPin("pin_2")).thenReturn(outputPin);
        tested.on(pin1);
        tested.on(pin2);

        verify(controller, times(1)).getProvisionedPin("pin_1");
        verify(controller, times(1)).getProvisionedPin("pin_2");
        verify(outputPin, times(2)).low();
    }

    @Test
    public void test_off_pcf8574() {
        DevicePinSupplierJoin pin1 = getPin(1, PinSupplierType.PCF8574, "");
        DevicePinSupplierJoin pin2 = getPin(2, PinSupplierType.PCF8574A, "");

        when(controller.getProvisionedPin("pin_1")).thenReturn(outputPin);
        when(controller.getProvisionedPin("pin_2")).thenReturn(outputPin);
        tested.off(pin1);
        tested.off(pin2);

        verify(controller, times(1)).getProvisionedPin("pin_1");
        verify(controller, times(1)).getProvisionedPin("pin_2");
        verify(outputPin, times(2)).high();
    }

    @Test
    public void test_unprovisionPins() {
        GpioPin pin1 = mock(GpioPin.class);
        GpioPin pin3 = mock(GpioPin.class);

        when(controller.getProvisionedPin("pin_1")).thenReturn(pin1);
        when(controller.getProvisionedPin("pin_3")).thenReturn(pin3);

        tested.unProvisionPins(Lists.newArrayList(1,2,3));

        verify(controller).unprovisionPin(pin1);
        verify(controller).unprovisionPin(pin3);
    }

    private DevicePinSupplierJoin getPin() {
        return getPin(1, PinSupplierType.RASBERRY_PI, "5");
    }

    private DevicePinSupplierJoin getPin(int pinId, PinSupplierType pinSupplierType, String pinHardwareId) {
        DevicePinSupplierJoin pin = new DevicePinSupplierJoin();
        pin.setPinId(pinId);
        pin.setPinSupplierType(pinSupplierType);
        pin.setPinNumber(0);
        pin.setPinSupplierHardwareId(pinHardwareId);
        return pin;
    }
}