package com.kevin_mic.aqua.entity;

import com.kevin_mic.aqua.types.DeviceType;
import lombok.Data;

@Data
public class Device {
    public static final String TABLE_NAME = "device";

    private int deviceId;
    private String pinId;
    private DeviceType type;
    private String name;
    private String hardwareId;
}
