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

import java.util.List;

@Data
public class PumpSchedule implements ActionInterface {
    @AllowedDeviceTypes(DeviceType.PumpAC)
    @Required
    @Owned
    private List<Integer> pumpIds;

    @Required
    @AllowedScheduleTypes({ScheduleType.AlwaysOn, ScheduleType.OnOff, ScheduleType.Interval})
    private ScheduleInterface schedule;

    private String name;
    private int actionId;
    private final ActionType actionType = ActionType.PumpSchedule;
}
