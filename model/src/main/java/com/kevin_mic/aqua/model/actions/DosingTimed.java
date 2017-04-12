package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Dosage;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.actions.metadata.SystemProvided;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ActionType;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

@Data
public class DosingTimed implements ActionInterface {
    @AllowedDeviceTypes(DeviceType.DosingPumpPeristalticTimed)
    @Required
    @Owned
    private Integer pumpId;

    @Required
    private Dosage dosage;

    @SystemProvided
    @AllowedScheduleTypes({ScheduleType.Interval, ScheduleType.Run})
    private ScheduleInterface dosingSchedule;

    private String name;
    private int actionId;
    private final ActionType type = ActionType.DosingTimed;
}
