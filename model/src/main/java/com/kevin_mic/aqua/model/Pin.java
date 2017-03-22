package com.kevin_mic.aqua.model;

import lombok.Data;

@Data
public class Pin {
    public static final String TABLE_NAME = "pin";

    private int pinId;
    private int pinNumber;
    private int pinSupplierId;
    private Integer ownedByDeviceId;
}
