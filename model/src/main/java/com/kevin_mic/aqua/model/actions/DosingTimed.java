package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Dosage;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.DeviceType;
import lombok.Data;

@Data
public class DosingTimed implements ActionInterface {
    @AllowedDeviceTypes(DeviceType.DosingPumpPeristalticTimed)
    @Required
    @Owned
    private int pumpId;

    @Required
    private Dosage dosage;

    @Required
    private int runOnMinute;

    private String name;
}
