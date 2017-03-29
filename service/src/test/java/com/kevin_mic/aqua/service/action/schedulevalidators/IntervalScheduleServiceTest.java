package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.IntervalSchedule;
import com.kevin_mic.aqua.model.types.TimeType;
import com.kevin_mic.aqua.service.ErrorType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class IntervalScheduleServiceTest {
    IntervalScheduleService tested;

    @Before
    public void before() {
        tested = new IntervalScheduleService();
    }

    @Test
    public void test_validate_success() {
        IntervalSchedule schedule = getIntervalSchedule();
        tested.validate("field", schedule);
    }

    @Test
    public void test_validate_invalidRepeat() {
        IntervalSchedule schedule = getIntervalSchedule();
        schedule.setRepeatInterval(-1);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.IntervalSchedule_InvalidRepeatInterval.name() + ":field");

        schedule.setRepeatInterval(0);
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.IntervalSchedule_InvalidRepeatInterval.name() + ":field");

    }

    @Test
    public void test_validate_minute_maxRepeat() {
        IntervalSchedule schedule = new IntervalSchedule();
        schedule.setRepeatInterval(IntervalScheduleService.MAX_MINUTE_REPEAT_INTERVAL);
        schedule.setTimeUnit(TimeType.Minute);
        tested.validate("field", schedule);

        schedule.setRepeatInterval(IntervalScheduleService.MAX_MINUTE_REPEAT_INTERVAL + 1);
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.IntervalSchedule_MaxMinuteRepeatIntervalExceeded.name() + ":field");
    }

    @Test
    public void test_validate_hour_maxRepeat() {
        IntervalSchedule schedule = new IntervalSchedule();
        schedule.setRepeatInterval(IntervalScheduleService.MAX_HOUR_REPEAT_INTERVAL);
        schedule.setTimeUnit(TimeType.Hour);
        tested.validate("field", schedule);

        schedule.setRepeatInterval(IntervalScheduleService.MAX_MINUTE_REPEAT_INTERVAL + 1);
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.IntervalSchedule_MaxHourRepeatIntervalExceeded.name() + ":field");
    }

    @Test
    public void test_validate_TimeUnit_required() {
        IntervalSchedule schedule = getIntervalSchedule();
        schedule.setTimeUnit(null);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.IntervalSchedule_TimeUnitRequired.name() + ":field");
    }

    private IntervalSchedule getIntervalSchedule() {
        IntervalSchedule schedule = new IntervalSchedule();
        schedule.setRepeatInterval(5);
        schedule.setTimeUnit(TimeType.Minute);
        return schedule;
    }

}