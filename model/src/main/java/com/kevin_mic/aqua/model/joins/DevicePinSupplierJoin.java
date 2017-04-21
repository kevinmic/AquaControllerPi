package com.kevin_mic.aqua.model.joins;

import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.types.PinType;
import lombok.Data;

@Data
public class DevicePinSupplierJoin {
    private int deviceId;
    private PinType pinType;
    private int pinId;
    private int pinNumber;
    private int pinSupplierId;
    private PinSupplierType pinSupplierType;
    private PinSupplierSubType pinSupplierSubType;
    private String pinSupplierHardwareId;
}
