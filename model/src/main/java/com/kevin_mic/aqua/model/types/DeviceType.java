package com.kevin_mic.aqua.model.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kevin_mic.aqua.model.types.PinType.*;
import static com.kevin_mic.aqua.model.types.PinSupplierSubType.*;

public enum DeviceType {
    I2C_BUS(false, false, null, I2C_SDA1, I2C_SLC1),
    SHIFT_REGISTER_BUS(false, false, null, SN74HC595_Clock, SN74HC595_LClock, SN74HC595_OutputEnabled, SN74HC595_Reset, SN74HC595_SER),
    THERMOMETER_BUS(false, false, null, I2C_THERMOMETER),

    PumpAC(true, true, Relay_120_VAC, Toggle),
    LightAC(true, true, Relay_120_VAC, Toggle),
    Heater(true, true, Relay_120_VAC, Toggle),
    FanAC(true, true, Relay_120_VAC, Toggle),
    FanDC(true, true, Relay_12_VDC, Toggle),
    DosingPumpPeristalticTimed(Relay_12_VDC, Toggle),
    DosingPumpPeristalticStepper(StepperArray, STEPPER_Direction, STEPPER_Step),
    FloatSwitch(SensorArray, Toggle),
    FluidLevelCapacitance(SensorArray),
    ThermometerI2C(null),
    WaterAlarm(SensorArray),
    NotificationLED(SensorArray)
    ;

    private final boolean canCreate;
    private final boolean allowDefaultOn;
    private final List<PinType> requiredPinTypes;
    private final PinSupplierSubType requiredPinSupplierSubType;

    DeviceType(PinSupplierSubType pinSupplierSubType, PinType ... requirePinTypes) {
        this(true, false, pinSupplierSubType, requirePinTypes);
    }

    DeviceType(boolean canCreate, boolean allowDefaultOn, PinSupplierSubType pinSupplierSubType, PinType ... requiredPinTypes) {
        this.canCreate = canCreate;
        this.allowDefaultOn = allowDefaultOn;
        this.requiredPinTypes = new ArrayList<>();
        this.requiredPinTypes.addAll(Arrays.asList(requiredPinTypes));
        this.requiredPinSupplierSubType = pinSupplierSubType;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public List<PinType> getRequiredPinTypes() {
        return requiredPinTypes;
    }

    public PinSupplierSubType getRequiredPinSupplierSubType() {
        return requiredPinSupplierSubType;
    };

    public boolean isAllowDefaultOn() {
        return allowDefaultOn;
    }
}
