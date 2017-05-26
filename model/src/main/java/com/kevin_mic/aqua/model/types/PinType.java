package com.kevin_mic.aqua.model.types;

public enum PinType {
    I2C_SDA1,
    I2C_SLC1,
    I2C_THERMOMETER,

    SN74HC595_OutputEnabled,
    SN74HC595_SER,
    SN74HC595_LClock,
    SN74HC595_Clock,
    SN74HC595_Reset,

    Toggle,
    STEPPER_Direction,
    STEPPER_Step,
}
