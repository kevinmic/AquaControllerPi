package com.kevin_mic.aqua.model.schedule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kevin_mic.aqua.model.types.DayOfWeek;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class OnOffScheduleTest {
    @Test
    public void testIsOn_on_day() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.minusMinutes(1);
        LocalDateTime after = now.plusMinutes(1);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.valueOf(now.getDayOfWeek())));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertTrue(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_off_day() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.minusMinutes(1);
        LocalDateTime after = now.plusMinutes(1);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.valueOf(now.plusDays(1).getDayOfWeek())));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertFalse(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_on_exactOn() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = now.plusMinutes(1);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.ALL_DAYS));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(now), new HourMinute(after))));

        assertTrue(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_on_inExactOn() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.minusMinutes(1);
        LocalDateTime after = now.plusMinutes(1);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.ALL_DAYS));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertTrue(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_off_before_inExactAfter() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.minusMinutes(3);
        LocalDateTime after = beforeNow.minusMinutes(2);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.ALL_DAYS));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertFalse(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_off_before_exactAfter() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.minusMinutes(3);
        LocalDateTime after = now;

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.ALL_DAYS));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertFalse(onOffSchedule.isOnNow());
    }

    @Test
    public void testIsOn_off_after() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow = now.plusMinutes(1);
        LocalDateTime after = now.plusMinutes(2);

        OnOffSchedule onOffSchedule = new OnOffSchedule();
        onOffSchedule.setDays(Sets.newHashSet(DayOfWeek.ALL_DAYS));
        onOffSchedule.setOnOffTimes(Lists.newArrayList(new OnOffTime(new HourMinute(beforeNow), new HourMinute(after))));

        assertFalse(onOffSchedule.isOnNow());
    }

}