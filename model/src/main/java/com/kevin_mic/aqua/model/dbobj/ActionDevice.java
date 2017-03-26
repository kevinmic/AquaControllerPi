package com.kevin_mic.aqua.model.dbobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionDevice {
    public static final String TABLE_NAME = "action_device";
    private int actionId;
    private int deviceId;
}
