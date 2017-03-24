package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Schedule;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.DeviceType;

import java.util.List;

public class AcLightControl {
    @AllowedDeviceTypes(DeviceType.LightAC)
    @Required
    @Owned
    private List<Integer> lightIds;

    @Required
    private Schedule schedule;
}
