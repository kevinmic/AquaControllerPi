package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.HourMinute;
import com.kevin_mic.aqua.model.schedule.RunSchedule;
import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.service.ErrorType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RunScheduleServiceTest {
    RunScheduleService tested;

    OnOffScheduleService onOffScheduleService;

    @Before
    public void before() {
        onOffScheduleService = mock(OnOffScheduleService.class);
        tested = new RunScheduleService(onOffScheduleService);
    }

    @Test
    public void test_validate_valid() {
        RunSchedule runSchedule = getRunSchedule();
        tested.validate("field", runSchedule);

        verify(onOffScheduleService).validateHourMinute("field", runSchedule.getRunTimes().get(0));
        verify(onOffScheduleService).validateDays("field", runSchedule.getDays());

        Mockito.verifyNoMoreInteractions(onOffScheduleService);
    }

    @Test
    public void test_validate_emptyRunTimes() {
        RunSchedule runSchedule = getRunSchedule();
        runSchedule.setRunTimes(null);
        assertThatThrownBy(() -> tested.validate("field", runSchedule)).hasMessage(ErrorType.ScheduleRunTimesRequired + ":field");

        runSchedule.setRunTimes(new ArrayList());
        assertThatThrownBy(() -> tested.validate("field", runSchedule)).hasMessage(ErrorType.ScheduleRunTimesRequired + ":field");
    }

    @Test
    public void test_validate_maxRunTimes() {
        RunSchedule runSchedule = getRunSchedule();
        List<HourMinute> list = new ArrayList<>();
        for (int i = 0; i < RunScheduleService.MAX_RUN; i++) {
            list.add(new HourMinute(i,0));
        }
        runSchedule.setRunTimes(list);

        tested.validate("field", runSchedule);

        list.add(new HourMinute(24,0));
        assertThatThrownBy(() -> tested.validate("field", runSchedule)).hasMessage(ErrorType.ScheduleRunTimesMaximumExceeded + ":field");
    }

    @Test
    public void test_validate_duplicateRunTime() {
        RunSchedule runSchedule = getRunSchedule();
        runSchedule.getRunTimes().add(runSchedule.getRunTimes().get(0));

        assertThatThrownBy(() -> tested.validate("field", runSchedule)).hasMessage(ErrorType.ScheduleRun_DuplicateRunTime + ":field");
    }

    @Test
    public void test_validate_runOrder() {
        RunSchedule runSchedule = getRunSchedule();

        HourMinute first = new HourMinute(0,0);
        HourMinute last = new HourMinute(23,59);
        runSchedule.setRunTimes(Arrays.asList(new HourMinute[] {last, first}));

        tested.validate("field", runSchedule);

        assertEquals(first, runSchedule.getRunTimes().get(0));
        assertEquals(last, runSchedule.getRunTimes().get(1));
    }

    private RunSchedule getRunSchedule() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.ALL_DAYS);

        List<HourMinute> runTimes = new ArrayList<>();
        runTimes.add(new HourMinute(1,1));

        RunSchedule runSchedule = new RunSchedule();
        runSchedule.setDays(daysOfWeek);
        runSchedule.setRunTimes(runTimes);
        return runSchedule;
    }
}