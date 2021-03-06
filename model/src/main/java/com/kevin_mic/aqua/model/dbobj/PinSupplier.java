package com.kevin_mic.aqua.model.dbobj;

import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.types.PinType;
import lombok.Data;

import java.util.List;

@Data
public class PinSupplier {
    public static final String TABLE_NAME = "pin_supplier";

    private int pinSupplierId;
    private PinSupplierType type;
    private PinSupplierSubType subType;
    private String name;
    private String hardwareId;
}
