package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.DosingTimed;
import com.kevin_mic.aqua.model.actions.LightSchedule;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.model.schedule.IntervalSchedule;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.schedule.RunSchedule;
import com.kevin_mic.aqua.service.ErrorType;
import lombok.Data;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScheduleServiceFactoryTest {
    ScheduleServiceFactory tested;

    private OnOffScheduleService onOffScheduleService;
    private IntervalScheduleService intervalScheduleService;
    private AlwaysOnScheduleService alwaysOnScheduleService;
    private RunScheduleService runScheduleService;
    private Scheduler scheduler;

    @Before
    public void before() {
        onOffScheduleService = mock(OnOffScheduleService.class);
        intervalScheduleService = mock(IntervalScheduleService.class);
        alwaysOnScheduleService = mock(AlwaysOnScheduleService.class);
        runScheduleService = mock(RunScheduleService.class);
        scheduler = mock(Scheduler.class);

        tested = new ScheduleServiceFactory(onOffScheduleService, intervalScheduleService, alwaysOnScheduleService, runScheduleService, scheduler);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(onOffScheduleService, intervalScheduleService, alwaysOnScheduleService, runScheduleService, scheduler);
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

    @Test
    public void test_deleteSchedule() throws SchedulerException {
        JobKey keyA = new JobKey("A");
        JobKey keyB = new JobKey("B");

        Set<JobKey> keys = new HashSet<>();
        keys.add(keyA);
        keys.add(keyB);

        when(scheduler.getJobKeys(any())).thenReturn(keys);
        tested.deleteSchedules(1);

        verify(scheduler).getJobKeys(GroupMatcher.groupEquals(ScheduleServiceFactory.getActionGroupName(1)));
        verify(scheduler).deleteJob(keyA);
        verify(scheduler).deleteJob(keyB);
    }

    @Test
    public void test_updateSchedule() throws SchedulerException {
        JobKey keyA = new JobKey("A");
        JobKey keyB = new JobKey("B");

        Set<JobKey> keys = new HashSet<>();
        keys.add(keyA);
        keys.add(keyB);

        OnOffSchedule onOffSchedule = new OnOffSchedule();

        List<ScheduleJob> jobs = new ArrayList<>();
        jobs.add(createScheduleJob());
        jobs.add(createScheduleJob());

        when(scheduler.getJobKeys(any())).thenReturn(keys);
        when(onOffScheduleService.getJobs(1, onOffSchedule)).thenReturn(jobs);

        PumpSchedule pumpSchedule = new PumpSchedule();
        pumpSchedule.setSchedule(onOffSchedule);
        pumpSchedule.setActionId(1);
        tested.updateSchedules(null, pumpSchedule);

        verify(scheduler).getJobKeys(GroupMatcher.groupEquals(ScheduleServiceFactory.getActionGroupName(1)));
        verify(scheduler).deleteJob(keyA);
        verify(scheduler).deleteJob(keyB);


        jobs.forEach(job ->{
            job.getTriggers().forEach(trg -> {
                try {
//                    System.out.println(trg);
                    verify(scheduler).scheduleJob(job.getJobDetail(), trg);
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        verify(onOffScheduleService).getJobs(1, onOffSchedule);
    }

    private ScheduleJob createScheduleJob() {
        List<Trigger> triggers = new ArrayList<>();
        triggers.add(mock(Trigger.class));
        triggers.add(mock(Trigger.class));

        ScheduleJob job = new ScheduleJob();
        job.setJobDetail(mock(JobDetail.class));
        job.setTriggers(triggers);

        return job;
    }
}