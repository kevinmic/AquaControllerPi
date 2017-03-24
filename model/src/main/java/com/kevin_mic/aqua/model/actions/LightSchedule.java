package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Schedule;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.DeviceType;
import lombok.Data;

import java.util.List;

@Data
public class LightSchedule implements ActionInterface {
    @AllowedDeviceTypes(DeviceType.LightAC)
    @Required
    @Owned
    private List<Integer> lightIds;

    @Required
    private Schedule schedule;

    private String name;
}
