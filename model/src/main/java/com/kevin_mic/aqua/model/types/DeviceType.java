package com.kevin_mic.aqua.model.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kevin_mic.aqua.model.types.PinType.*;
import static com.kevin_mic.aqua.model.types.PinSupplierSubType.*;

public enum DeviceType {
    I2C_BUS(false, null, I2C_SDA1, I2C_SLC1),
    SHIFT_REGISTER_BUS(false, null, SN74HC595_Clock, SN74HC595_LClock, SN74HC595_OutputEnabled, SN74HC595_Reset, SN74HC595_SER),

    PumpAC(Relay_120_VAC, Toggle),
    LightAC(Relay_120_VAC, Toggle),
    Heater(Relay_120_VAC, Toggle),
    FanAC(Relay_120_VAC, Toggle),
    FanDC(Relay_12_VDC, Toggle),
    DosingPumpPeristalticTimed(Relay_12_VDC, Toggle),
    DosingPumpPeristalticStepper(StepperArray, STEPPER_Direction, STEPPER_Step),
    FloatSwitch(SensorArray, Toggle),
    FluidLevelCapacitance(SensorArray),
    ThermometerI2C(null),
    WaterAlarm(SensorArray),
    NotificationLED(SensorArray)
    ;

    private final boolean canCreate;
    private final List<PinType> requiredPinTypes;
    private final PinSupplierSubType requiredPinSupplierSubType;

    DeviceType(PinSupplierSubType pinSupplierSubType, PinType ... requirePinTypes) {
        this(true, pinSupplierSubType, requirePinTypes);
    }

    DeviceType(boolean canCreate, PinSupplierSubType pinSupplierSubType, PinType ... requiredPinTypes) {
        this.canCreate = canCreate;
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
}
