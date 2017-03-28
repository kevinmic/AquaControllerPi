package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ActionType;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.ScheduleType;
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
    @AllowedScheduleTypes({ScheduleType.Interval})
    private ScheduleInterface schedule;

    private String name;
    private int actionId;

    private final ActionType actionType = ActionType.TopOff;
}
