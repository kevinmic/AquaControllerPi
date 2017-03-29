package com.kevin_mic.aqua.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HourMinute implements Comparable<HourMinute> {
    private int hour;
    private int minute;

    public boolean isBefore(HourMinute lastOn) {
        return getMinuteOfDay() < lastOn.getMinuteOfDay();
    }

    public int getMinuteOfDay() {
        return (hour * 60) + minute;
    }

    @Override
    public int compareTo(HourMinute obj) {
        return Integer.valueOf(getMinuteOfDay()).compareTo(obj.getMinuteOfDay());
    }
}
