package com.kevin_mic.aqua.model.actions;

import com.kevin_mic.aqua.model.Dosage;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.Owned;
import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.ActionType;
import com.kevin_mic.aqua.model.types.DeviceType;
import lombok.Data;

@Data
public class DosingStepper implements ActionInterface {
    @AllowedDeviceTypes(DeviceType.DosingPumpPeristalticStepper)
    @Required
    @Owned
    private int pumpId;

    @Required
    private Dosage dosage;

    @Required
    private int runOnMinute;

    private String name;
    private int actionId;
    private final ActionType actionType = ActionType.DosingStepper;
}