package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AlwaysOnScheduleServiceTest {
    AlwaysOnScheduleService tested;

    OnOffScheduleService onOffScheduleService;

    @Before
    public void before() {
        onOffScheduleService = mock(OnOffScheduleService.class);
        tested = new AlwaysOnScheduleService(onOffScheduleService);
    }

    @Test
    public void test_validate() {
        tested.validate(null, null);
    }

    @Test
    public void test_getJobs() {
        List<ScheduleJob> jobs = tested.getJobs(1, new AlwaysOnSchedule());

        verify(onOffScheduleService).createJob(1, true);
        verify(onOffScheduleService).loadImmediateTrigger(1);
    }


}