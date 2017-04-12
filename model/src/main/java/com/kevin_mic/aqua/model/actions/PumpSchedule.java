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
public class PumpSchedule implements OnOffInterface {
    @AllowedDeviceTypes(DeviceType.PumpAC)
    @Required
    @Owned
    private List<Integer> deviceIds;

    @Required
    @AllowedScheduleTypes({ScheduleType.AlwaysOn, ScheduleType.OnOff})
    private ScheduleInterface schedule;

    private String name;
    private int actionId;
    private final ActionType type = ActionType.PumpSchedule;
}
