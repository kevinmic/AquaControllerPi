package com.kevin_mic.aqua.model.types;

import java.util.Arrays;
import java.util.List;

public enum DayOfWeek {
    Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7), ALL_DAYS(1,2,3,4,5,6,7);

    private final Integer[] cronDays;

    DayOfWeek(Integer ... cronDays) {
        this.cronDays = cronDays;
    }

    public List<Integer> getCronDays() {
        return Arrays.asList(cronDays);
    }
}
