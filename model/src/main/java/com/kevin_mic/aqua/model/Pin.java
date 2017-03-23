package com.kevin_mic.aqua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pin {
    public static final String TABLE_NAME = "pin";

    private int pinId;
    private int pinNumber;
    private int pinSupplierId;
    private Integer ownedByDeviceId;
}
