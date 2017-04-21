package com.kevin_mic.aqua.model.schedule;

import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.model.types.ScheduleType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.Set;

@Data
public class OnOffSchedule implements ScheduleInterface {
    private final ScheduleType type = ScheduleType.OnOff;

    Set<DayOfWeek> days;
    List<OnOffTime> onOffTimes;

    public boolean isOnNow() {
        LocalDateTime now = LocalDateTime.now();

        if (!days.contains(DayOfWeek.ALL_DAYS) && !days.contains(DayOfWeek.valueOf(now.getDayOfWeek()))) {
            return false;
        }

        // Check time
        days.contains(now.getDayOfWeek());

        return onOffTimes.stream()
                .filter(onOff -> !now.isBefore(getLocalForHourMinute(onOff.getOn())))
                .anyMatch(onOff -> now.isBefore(getLocalForHourMinute(onOff.getOff())));
    }

    private LocalDateTime getLocalForHourMinute(HourMinute hm) {
        LocalDate time = LocalDate.now();
        return time.atTime(hm.getHour(), hm.getMinute());
    }
}
