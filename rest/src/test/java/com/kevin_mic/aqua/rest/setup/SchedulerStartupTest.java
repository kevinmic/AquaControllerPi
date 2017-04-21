package com.kevin_mic.aqua.rest.setup;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.actions.metadata.Schedule;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.action.ActionService;
import com.kevin_mic.aqua.service.jobs.OnOffJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchedulerStartupTest {
    SchedulerStartup tested;

    Scheduler scheduler;
    ActionService actionService;

    @Before
    public void before() {
        scheduler = mock(Scheduler.class);
        actionService = mock(ActionService.class);

        tested = new SchedulerStartup(scheduler, actionService);
    }

    @Test
    public void start_alwaysOn() throws Exception {
        ActionInterface alwaysOnMock = mock(ActionInterface.class);
        when(alwaysOnMock.getActionId()).thenReturn(1);

        List<ActionInterface> alwaysOn = Lists.newArrayList(alwaysOnMock);
        List<ActionInterface> onOff= new ArrayList<>();

        when(actionService.findActionsByScheduleType(ScheduleType.AlwaysOn)).thenReturn(alwaysOn);
        when(actionService.findActionsByScheduleType(ScheduleType.OnOff)).thenReturn(onOff);

        JobKey key = new JobKey("KEY");
        Set<JobKey> keys = Sets.newHashSet(key);

        when(scheduler.getJobKeys(GroupMatcher.groupEquals("action_1"))).thenReturn(keys);
        tested.start();

        verify(scheduler).start();
        verify(actionService).findActionsByScheduleType(ScheduleType.AlwaysOn);
        verify(scheduler).triggerJob(key);
    }

    @Test
    public void start_onOff() throws Exception {
        ActionInterface onOffMock = mock(ActionInterface.class);
        OnOffSchedule onOffScheduleMock = mock(OnOffSchedule.class);

        when(onOffMock.getActionId()).thenReturn(2);
        when(onOffMock.findSchedule()).thenReturn(onOffScheduleMock);
        when(onOffScheduleMock.isOnNow()).thenReturn(true);


        List<ActionInterface> alwaysOn = new ArrayList<>();

        List<ActionInterface> onOff= Lists.newArrayList(onOffMock);

        when(actionService.findActionsByScheduleType(ScheduleType.AlwaysOn)).thenReturn(alwaysOn);
        when(actionService.findActionsByScheduleType(ScheduleType.OnOff)).thenReturn(onOff);

        JobKey onKey = new JobKey("on");
        JobKey offKey = new JobKey("off");
        Set<JobKey> keys = Sets.newHashSet(onKey, offKey);

        when(scheduler.getJobKeys(GroupMatcher.groupEquals("action_2"))).thenReturn(keys);
        tested.start();

        verify(scheduler).start();
        verify(actionService).findActionsByScheduleType(ScheduleType.OnOff);
        verify(scheduler).triggerJob(onKey);
    }

    @Test
    public void test_stop() throws Exception {
        tested.stop();
        verify(scheduler).shutdown(false);
    }

}