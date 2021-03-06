package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.actions.metadata.Schedule;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ActionType;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

import java.util.List;

@Data
@AllowedScheduleTypes({ScheduleType.AlwaysOn,ScheduleType.OnOff})
public class LightSchedule implements OnOffInterface {
    @AllowedDeviceTypes(DeviceType.LightAC)
    @Required
    @Owned
    private List<Integer> deviceIds;

    @Required
    @Schedule
    private ScheduleInterface schedule;

    private String name;
    private int actionId;
    private final ActionType type = ActionType.LightSchedule;
}
