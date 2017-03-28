package com.kevin_mic.aqua.model.schedule;

import com.kevin_mic.aqua.model.actions.metadata.Required;
import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class IntervalSchedule implements ScheduleInterface {
    private final ScheduleType type = ScheduleType.Interval;

    private int repeatInterval;
    private TimeUnit timeUnit;
}
