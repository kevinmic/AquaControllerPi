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
    SupplierCannotBeNull, ActionFieldCannotBeEmpty, CannotChangeActionType, ScheduleRequired, ScheduleDaysRequired, ScheduleDays_AllDaysCannotBeWithWeekDays, ScheduleOnOffTimesRequired, ScheduleOnOffTimesMaximumExceeded, ScheduleOnOffInvalid, ScheduleOnOff_OnOutOfOrder, ScheduleOnOff_On_SameAsOff, ScheduleOnOff_On_OutOfOrder, ScheduleOnOff_Off_Duplicate, ScheduleOnOff_Off_ConflictsWithFirstOn, ScheduleOnOff_Off_OnlyTheLastOffCanBeBeforeOn, IntervalSchedule_InvalidRepeatInterval, IntervalSchedule_TimeUnitRequired, IntervalSchedule_MaxMinuteRepeatIntervalExceeded, IntervalSchedule_MaxHourRepeatIntervalExceeded, ScheduleRunTimesRequired, ScheduleRunTimesMaximumExceeded, ScheduleRun_DuplicateRunTime, ScheduleOnOff_RunTimeOutOfOrder, InvalidHour, InvalidMinute, ScheduleOnOff_On_ConflictsWithPreviousOff, InvalidScheduleTypeForField, IdMismatch,
}
