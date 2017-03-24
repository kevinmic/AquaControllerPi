package com.kevin_mic.aqua.model.dbobj;

import com.kevin_mic.aqua.model.types.PinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevicePin {
    public static final String TABLE_NAME = "device_pin";

    private int pinId;
    private int deviceId;
    private PinType pinType;
}
