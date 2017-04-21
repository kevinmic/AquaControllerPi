package com.kevin_mic.aqua.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HourMinute implements Comparable<HourMinute> {
    private int hour;
    private int minute;

    public HourMinute(LocalDateTime now) {
        this.hour = now.getHour();
        this.minute = now.getMinute();
    }

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
