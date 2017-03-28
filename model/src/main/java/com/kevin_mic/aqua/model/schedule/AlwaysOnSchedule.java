package com.kevin_mic.aqua.model.schedule;

import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

@Data
public class AlwaysOnSchedule implements ScheduleInterface {
    private final ScheduleType type = ScheduleType.AlwaysOn;
}
