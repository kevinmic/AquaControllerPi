package com.kevin_mic.aqua.model.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kevin_mic.aqua.model.types.PinType.STEPPER_Direction;
import static com.kevin_mic.aqua.model.types.PinType.STEPPER_Step;
import static com.kevin_mic.aqua.model.types.PinType.Toggle;

public enum DeviceType {
    I2C_BUS(false),
    SHIFT_REGISTER_BUS(false),

    PumpAC(Toggle),
    Light(Toggle),
    DosingPumpPeristalticTimed(Toggle),
    DosingPumpPeristalticStepper(STEPPER_Direction, STEPPER_Step),
    FloatSwitch(Toggle),
    FluidLevelCapacitance(),
    ThermometerI2C,
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
}
