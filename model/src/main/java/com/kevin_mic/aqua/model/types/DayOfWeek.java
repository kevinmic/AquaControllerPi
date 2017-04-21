package com.kevin_mic.aqua.model.types;

import java.util.Arrays;
import java.util.List;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

public enum DayOfWeek {
    Sunday(SUNDAY, 1),
    Monday(MONDAY, 2),
    Tuesday(TUESDAY, 3),
    Wednesday(WEDNESDAY, 4),
    Thursday(THURSDAY, 5),
    Friday(FRIDAY, 6),
    Saturday(SATURDAY, 7),
    ALL_DAYS(null, 1,2,3,4,5,6,7);

    private final Integer[] cronDays;
    private final java.time.DayOfWeek timeDayOfWeek;

    DayOfWeek(java.time.DayOfWeek timeDayOfWeek,  Integer ... cronDays) {
        this.timeDayOfWeek = timeDayOfWeek;
        this.cronDays = cronDays;
    }

    public java.time.DayOfWeek getTimeDayOfWeek() {
        return timeDayOfWeek;
    }

    public List<Integer> getCronDays() {
        return Arrays.asList(cronDays);
    }

    public static DayOfWeek valueOf(java.time.DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return Monday;
            case TUESDAY:
                return Tuesday;
            case WEDNESDAY:
                return Wednesday;
            case THURSDAY:
                return Thursday;
            case FRIDAY:
                return Friday;
            case SATURDAY:
                return Saturday;
            case SUNDAY:
                return Sunday;
            default:
                throw new RuntimeException("Unhandled day Of Week :" + dayOfWeek);
        }
    }
}
