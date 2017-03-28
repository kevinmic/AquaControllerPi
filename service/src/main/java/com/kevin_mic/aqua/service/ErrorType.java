package com.kevin_mic.aqua.service;

public enum ErrorType {
    SupplierNameCannotBeNull,
    SupplierHardwareIdCannotBeNull,
    SupplierTypeCannotBeNull,
    SupplierHardwareIdAlreadyUsed,
    DeviceNameRequired,
    DeviceTypeRequired,
    CannotCreateThisDeviceType,
    InvalidPinTypes,
    WrongNumberOfPins,
    PinAlreadyOwned,
    PinsInUse,
    SupplierSubTypeCannotBeNull,
    SupplierSubTypeIncompatibleWithType,
    InvalidPinSupplierSubType,
    ActionNameRequried,
    ActionTypeRequired,
    DeviceAlreadyOwnedByAnotherAction,
    ActionFieldCannotBeNull,
    InvalidDeviceType,
    SupplierCannotBeNull, ActionFieldCannotBeEmpty, CannotChangeActionType,
}
