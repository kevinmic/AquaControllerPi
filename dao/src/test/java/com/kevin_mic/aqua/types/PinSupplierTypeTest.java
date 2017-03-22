package com.kevin_mic.aqua.types;

import com.kevin_mic.aqua.entity.PinSupplier;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PinSupplierTypeTest {
    @Test
    public void test_SN74HC595() {
        List<String> pinIds = PinSupplierType.SN74HC595.getPinIds(5, "2");
        assertEquals(8, pinIds.size());
        assertTrue(pinIds.contains("5:0"));
        assertTrue(pinIds.contains("5:1"));
        assertTrue(pinIds.contains("5:2"));
        assertTrue(pinIds.contains("5:3"));
        assertTrue(pinIds.contains("5:4"));
        assertTrue(pinIds.contains("5:5"));
        assertTrue(pinIds.contains("5:6"));
        assertTrue(pinIds.contains("5:7"));

        assertEquals(0, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:0"));
        assertEquals(1, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:1"));
        assertEquals(2, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:2"));
        assertEquals(3, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:3"));
        assertEquals(4, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:4"));
        assertEquals(5, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:5"));
        assertEquals(6, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:6"));
        assertEquals(7, PinSupplierType.SN74HC595.convertPinIdToPinNumber("1", "5:7"));
        assertEquals(8, PinSupplierType.SN74HC595.convertPinIdToPinNumber("2", "5:0"));
        assertEquals(11, PinSupplierType.SN74HC595.convertPinIdToPinNumber("2", "5:3"));
    }

}