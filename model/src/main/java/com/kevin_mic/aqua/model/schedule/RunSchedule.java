package com.kevin_mic.aqua.model.schedule;

import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RunSchedule implements ScheduleInterface {
    private final ScheduleType type = ScheduleType.Run;

    Set<DayOfWeek> days;
    List<HourMinute> runTimes;
}
