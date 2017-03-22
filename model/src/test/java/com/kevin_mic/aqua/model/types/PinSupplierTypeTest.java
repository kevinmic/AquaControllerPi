package com.kevin_mic.aqua.model.types;

import org.junit.Test;

import static org.junit.Assert.*;

public class PinSupplierTypeTest {
    @Test
    public void test_SN74HC595() {
        assertEquals(0, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 0));
        assertEquals(1, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 1));
        assertEquals(2, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 2));
        assertEquals(3, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 3));
        assertEquals(4, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 4));
        assertEquals(5, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 5));
        assertEquals(6, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 6));
        assertEquals(7, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", 7));
        assertEquals(8, PinSupplierType.SN74HC595.convertPinIdToPinNumber("2", 0));
        assertEquals(11, PinSupplierType.SN74HC595.convertPinIdToPinNumber("2", 3));
    }

    @Test
    public void test_PCF8574() {
        assertEquals(0, PinSupplierType.PCF8574.convertPinIdToPinNumber("A45", 0));
        assertEquals(1, PinSupplierType.PCF8574.convertPinIdToPinNumber("A45", 1));
        assertEquals(255, PinSupplierType.PCF8574.convertPinIdToPinNumber("A45", 255));
    }

}