package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.schedule.HourMinute;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.schedule.OnOffTime;
import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.jobs.OnOffJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class OnOffScheduleServiceTest {
    OnOffScheduleService tested;

    @Before
    public void before() {
        tested = new OnOffScheduleService();
    }

    @Test
    public void test_validate_valid() {
        OnOffSchedule schedule = getOnOffSchedule();

        tested.validate("field", schedule);
    }

    @Test
    public void test_validate_nullDays() {
        OnOffSchedule schedule = getOnOffSchedule();
        schedule.setDays(null);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleDaysRequired + ":field");

        schedule.setDays(new HashSet<>());
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleDaysRequired + ":field");
    }

    @Test
    public void test_validate_invalidDays() {
        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.ALL_DAYS);
        days.add(DayOfWeek.Monday);

        OnOffSchedule schedule = getOnOffSchedule();
        schedule.setDays(days);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleDays_AllDaysCannotBeWithWeekDays + ":field");
    }

    @Test
    public void test_validate_onOffRequired() {
        OnOffSchedule schedule = getOnOffSchedule();

        schedule.setOnOffTimes(null);
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOffTimesRequired + ":field");

        schedule.setOnOffTimes(new ArrayList<>());
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOffTimesRequired + ":field");
    }

    @Test
    public void test_validate_onBeforeOff_validIfLast_single() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1,0), new HourMinute(0,5)));
        schedule.setOnOffTimes(list);

        tested.validate("field", schedule);
    }

    @Test
    public void test_validate_onBeforeOff_validIfLast_multi() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1,0), new HourMinute(2,5)));
        list.add(new OnOffTime(new HourMinute(5,0), new HourMinute(6,5)));
        list.add(new OnOffTime(new HourMinute(7,0), new HourMinute(0,5)));
        schedule.setOnOffTimes(list);

        tested.validate("field", schedule);
    }

    @Test
    public void test_validate_onBeforeOff_validIfLast_multi_outOfOrder() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1,0), new HourMinute(2,5)));
        list.add(new OnOffTime(new HourMinute(7,0), new HourMinute(0,5)));
        list.add(new OnOffTime(new HourMinute(5,0), new HourMinute(6,5)));
        schedule.setOnOffTimes(list);

        tested.validate("field", schedule);
    }

    @Test
    public void test_validate_onBeforeOff_invalid() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1,0), new HourMinute(2,5)));
        list.add(new OnOffTime(new HourMinute(5,0), new HourMinute(0,5))); // This can only work if it is the last entry
        list.add(new OnOffTime(new HourMinute(7,0), new HourMinute(8,5)));
        schedule.setOnOffTimes(list);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOff_Off_OnlyTheLastOffCanBeBeforeOn + ":field");
    }

    @Test
    public void test_validate_onBeforeOff_sameOnOff() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1, 0), new HourMinute(1, 0)));
        schedule.setOnOffTimes(list);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOffInvalid + ":field");
    }

    @Test
    public void test_validate_onBeforeOff_on() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1, 0), new HourMinute(1, 1)));
        list.add(new OnOffTime(new HourMinute(1, 0), new HourMinute(1, 2)));
        schedule.setOnOffTimes(list);

        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOff_On_ConflictsWithPreviousOff + ":field");
    }

    @Test
    public void test_validate_onBeforeOff_tooMany() {
        OnOffSchedule schedule = getOnOffSchedule();

        List<OnOffTime> list = new ArrayList<>();
        list.add(new OnOffTime(new HourMinute(1,0), new HourMinute(1,1)));
        list.add(new OnOffTime(new HourMinute(1,2), new HourMinute(1,3)));
        list.add(new OnOffTime(new HourMinute(1,4), new HourMinute(1,5)));
        list.add(new OnOffTime(new HourMinute(1,6), new HourMinute(1,7)));
        list.add(new OnOffTime(new HourMinute(1,8), new HourMinute(1,9)));
        list.add(new OnOffTime(new HourMinute(1,10), new HourMinute(1,11)));
        schedule.setOnOffTimes(list);

        tested.validate("field", schedule);

        list.add(new OnOffTime(new HourMinute(1,12), new HourMinute(1,13)));
        assertThatThrownBy(() -> tested.validate("field", schedule)).hasMessage(ErrorType.ScheduleOnOffTimesMaximumExceeded+ ":field");
    }

    @Test
    public void test_validate_hourMinute() {
        assertThatThrownBy(() -> tested.validateHourMinute("field", new HourMinute(-1, 0))).hasMessage(ErrorType.InvalidHour + ":field");
        assertThatThrownBy(() -> tested.validateHourMinute("field", new HourMinute(24, 0))).hasMessage(ErrorType.InvalidHour + ":field");
        assertThatThrownBy(() -> tested.validateHourMinute("field", new HourMinute(0, -1))).hasMessage(ErrorType.InvalidMinute + ":field");
        assertThatThrownBy(() -> tested.validateHourMinute("field", new HourMinute(0, 60))).hasMessage(ErrorType.InvalidMinute + ":field");
        tested.validateHourMinute("field", new HourMinute(0,0));
        tested.validateHourMinute("field", new HourMinute(23,59));
    }

    @Test
    public void test_createSchedule() {
        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.Monday);
        days.add(DayOfWeek.Wednesday);
        Integer[] dayArray = {2, 4};

        List<OnOffTime> onOffTimes = new ArrayList<>();
        onOffTimes.add(new OnOffTime(new HourMinute(2, 0), new HourMinute(3,1)));
        onOffTimes.add(new OnOffTime(new HourMinute(4, 2), new HourMinute(5,3)));

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(days);
        onOffSchedule.setOnOffTimes(onOffTimes);

        List<ScheduleJob> jobs = tested.getJobs(1, onOffSchedule);

        assertEquals(2, jobs.size());

        ScheduleJob job = jobs.get(0);
        verifyJob(job, "on", true);
        verifyTrigger(job.getTriggers().get(0), "on_1", 2, 0, dayArray);
        verifyTrigger(job.getTriggers().get(1), "on_2", 4, 2, dayArray);

        job = jobs.get(1);
        verifyJob(job, "off", false);
        verifyTrigger(job.getTriggers().get(0), "off_1", 3, 1, dayArray);
        verifyTrigger(job.getTriggers().get(1), "off_2", 5, 3, dayArray);
    }

    private void verifyJob(ScheduleJob job, String name, boolean on) {
        assertEquals(
                job.getJobDetail(),
                newJob(OnOffJob.class)
                        .withIdentity(name, "action_1")
                        .usingJobData(OnOffJob.ACTION_ID, 1)
                        .usingJobData(OnOffJob.ON, on)
                        .build()
        );

    }

    private void verifyTrigger(Trigger trigger, String name, int hour, int minute, Integer[] dayArray) {
        assertEquals(
                trigger,
                newTrigger()
                        .withIdentity(name, "action_1")
                        .startNow()
                        .withSchedule(
                                atHourAndMinuteOnGivenDaysOfWeek(hour, minute, dayArray)
                        ).build()
        );
    }


    private OnOffSchedule getOnOffSchedule() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.ALL_DAYS);

        OnOffSchedule schedule = new OnOffSchedule();
        schedule.setDays(daysOfWeek);

        List<OnOffTime> onOffTimes = new ArrayList<>();
        onOffTimes.add(new OnOffTime(new HourMinute(0,0), new HourMinute(0,1)));
        schedule.setOnOffTimes(onOffTimes);
        return schedule;
    }
}