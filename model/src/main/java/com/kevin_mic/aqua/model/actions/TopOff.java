package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Schedule;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.DeviceType;
import lombok.Data;

@Data
public class TopOff implements ActionInterface {
    @AllowedDeviceTypes({DeviceType.DosingPumpPeristalticTimed, DeviceType.PumpAC})
    @Required
    @Owned
    private int pumpId;

    @AllowedDeviceTypes(DeviceType.FloatSwitch)
    @Required
    private int tankWaterLevelFloat;

    @AllowedDeviceTypes(DeviceType.FloatSwitch)
    private int refillReserviorFloat;

    @Required
    private int maxRunTimeSeconds;

    @Required
    private Schedule schedule;

    private String name;
}
