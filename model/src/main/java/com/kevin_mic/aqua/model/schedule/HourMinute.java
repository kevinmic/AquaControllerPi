package com.kevin_mic.aqua.model.schedule;

import lombok.Data;

@Data
public class HourMinute {
    private int hour;
    private int minute;

    public boolean isBefore(HourMinute lastOn) {
        return getMinuteOfDay() < lastOn.getMinuteOfDay();
    }

    public int getMinuteOfDay() {
        return (hour * 60) + minute;
    }
}
