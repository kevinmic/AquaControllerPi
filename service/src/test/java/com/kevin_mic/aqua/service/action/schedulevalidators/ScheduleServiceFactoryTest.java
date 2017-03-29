package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.DosingTimed;
import com.kevin_mic.aqua.model.actions.LightSchedule;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.model.schedule.IntervalSchedule;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.schedule.RunSchedule;
import com.kevin_mic.aqua.service.ErrorType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ScheduleServiceFactoryTest {
    ScheduleServiceFactory tested;

    private OnOffScheduleService onOffScheduleService;
    private IntervalScheduleService intervalScheduleService;
    private AlwaysOnScheduleService alwaysOnScheduleService;
    private RunScheduleService runScheduleService;

    @Before
    public void before() {
        onOffScheduleService = mock(OnOffScheduleService.class);
        intervalScheduleService = mock(IntervalScheduleService.class);
        alwaysOnScheduleService = mock(AlwaysOnScheduleService.class);
        runScheduleService = mock(RunScheduleService.class);

        tested = new ScheduleServiceFactory(onOffScheduleService, intervalScheduleService, alwaysOnScheduleService, runScheduleService);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(onOffScheduleService, intervalScheduleService, alwaysOnScheduleService, runScheduleService);
    }

    @Test
    public void test_null() {
        LightSchedule action = new LightSchedule();

        assertThatThrownBy(() -> tested.validateSchedules(action)).hasMessage(ErrorType.ScheduleRequired + ":schedule");
    }

    @Test
    public void test_invalidScheduleType() {
        LightSchedule action = new LightSchedule();
        action.setSchedule(new RunSchedule());

        assertThatThrownBy(() -> tested.validateSchedules(action)).hasMessage(ErrorType.InvalidScheduleTypeForField + ":schedule");
    }

    @Test
    public void test_onOff() {
        LightSchedule action = new LightSchedule();
        action.setSchedule(new OnOffSchedule());

        tested.validateSchedules(action);
        verify(onOffScheduleService).validate(anyString(), (OnOffSchedule) notNull());
    }

    @Test
    public void test_alwaysOn() {
        LightSchedule action = new LightSchedule();
        action.setSchedule(new AlwaysOnSchedule());

        tested.validateSchedules(action);
        verify(alwaysOnScheduleService).validate(anyString(), (AlwaysOnSchedule) notNull());
    }

    @Test
    public void test_runSchedule() {
        DosingTimed action = new DosingTimed();
        action.setDosingSchedule(new RunSchedule());

        tested.validateSchedules(action);
        verify(runScheduleService).validate(anyString(), (RunSchedule) notNull());
    }

    @Test
    public void test_intervalSchedule() {
        DosingTimed action = new DosingTimed();
        action.setDosingSchedule(new IntervalSchedule());

        tested.validateSchedules(action);
        verify(intervalScheduleService).validate(anyString(), (IntervalSchedule) notNull());
    }

}