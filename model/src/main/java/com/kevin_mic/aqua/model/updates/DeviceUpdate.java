package com.kevin_mic.aqua.model.updates;

import com.kevin_mic.aqua.model.dbobj.DevicePin;
import lombok.Data;

import java.util.List;

@Data
public class DeviceUpdate {
    private String name;
    private List<DevicePin> pins;
    private String hardwareId;
}
