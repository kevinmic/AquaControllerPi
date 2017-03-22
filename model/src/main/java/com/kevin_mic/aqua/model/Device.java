package com.kevin_mic.aqua.model;

import com.kevin_mic.aqua.model.types.DeviceType;
import lombok.Data;

import java.util.List;

@Data
public class Device {
    public static final String TABLE_NAME = "device";

    private int deviceId;
    private DeviceType type;
    private String name;
    private String hardwareId;

    List<DevicePin> pins;
}
