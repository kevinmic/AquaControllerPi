package com.kevin_mic.aqua.entity;

import com.kevin_mic.aqua.types.PinSupplierType;
import com.kevin_mic.aqua.types.Voltage;
import lombok.Data;

import java.util.List;

@Data
public class PinSupplier {
    public static final String TABLE_NAME = "pin_supplier";

    private int pinSupplierId;
    private PinSupplierType supplierType;
    private String name;
    private Voltage voltage;
    private String hardwareId;

    //
    private List<Pin> pins;
}
