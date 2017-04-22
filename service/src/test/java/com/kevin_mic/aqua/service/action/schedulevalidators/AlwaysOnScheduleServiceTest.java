package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(onOffScheduleService.createJob(1, true)).thenReturn(mock(JobDetail.class));
        when(onOffScheduleService.loadImmediateTrigger(1)).thenReturn(mock(Trigger.class));

        List<ScheduleJob> jobs = tested.getJobs(1, new AlwaysOnSchedule());
        assertEquals(1, jobs.size());
        assertNotNull(jobs.get(0).getJobDetail());
        assertNotNull(jobs.get(0).getTriggers());
        assertEquals(1, jobs.get(0).getTriggers().size());

        verify(onOffScheduleService).createJob(1, true);
        verify(onOffScheduleService).loadImmediateTrigger(1);
    }


}