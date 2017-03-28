package com.kevin_mic.aqua.model.schedule;

import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.model.types.TimeType;
import lombok.Data;

@Data
public class IntervalSchedule implements ScheduleInterface {
    private final ScheduleType type = ScheduleType.Interval;

    private int repeatInterval;
    private TimeType timeUnit;
}
