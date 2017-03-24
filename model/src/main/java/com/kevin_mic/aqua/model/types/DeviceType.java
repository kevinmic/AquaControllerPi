package com.kevin_mic.aqua.model.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kevin_mic.aqua.model.types.PinType.I2C_SDA1;
import static com.kevin_mic.aqua.model.types.PinType.I2C_SLC1;
import static com.kevin_mic.aqua.model.types.PinType.SN74HC595_Clock;
import static com.kevin_mic.aqua.model.types.PinType.SN74HC595_LClock;
import static com.kevin_mic.aqua.model.types.PinType.SN74HC595_OutputEnabled;
import static com.kevin_mic.aqua.model.types.PinType.SN74HC595_Reset;
import static com.kevin_mic.aqua.model.types.PinType.SN74HC595_SER;
import static com.kevin_mic.aqua.model.types.PinType.STEPPER_Direction;
import static com.kevin_mic.aqua.model.types.PinType.STEPPER_Step;
import static com.kevin_mic.aqua.model.types.PinType.Toggle;

public enum DeviceType {
    I2C_BUS(false, I2C_SDA1, I2C_SLC1),
    SHIFT_REGISTER_BUS(false, SN74HC595_Clock, SN74HC595_LClock, SN74HC595_OutputEnabled, SN74HC595_Reset, SN74HC595_SER),

    PumpAC(Toggle),
    Light(Toggle),
    DosingPumpPeristalticTimed(Toggle),
    DosingPumpPeristalticStepper(STEPPER_Direction, STEPPER_Step),
    FloatSwitch(Toggle),
    FluidLevelCapacitance(),
    ThermometerI2C,
    WaterAlarm,
    NotificationLED
    ;

    private final boolean canCreate;
    private final List<PinType> requiredPinTypes;

    DeviceType(PinType ... requirePinTypes) {
        this(true, requirePinTypes);
    }

    DeviceType(boolean canCreate, PinType ... requiredPinTypes) {
        this.canCreate = canCreate;
        this.requiredPinTypes = new ArrayList<>();
        this.requiredPinTypes.addAll(Arrays.asList(requiredPinTypes));
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public List<PinType> getRequiredPinTypes() {
        return requiredPinTypes;
    }
}
