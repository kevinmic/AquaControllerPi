package com.kevin_mic.aqua.entity;

import com.kevin_mic.aqua.types.PumpCalibrationType;

public class PumpCalibration {
    public static final String TABLE_NAME = "pump_calibration";

    private int deviceId;
    private PumpCalibrationType type;
    private String volume;
}
