package com.kevin_mic.aqua.service.jobs;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.service.gpio.PinController;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Arrays;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnOffJobTest {
    public static final int ACTION_ID = 1;
    public static final int DEVICEID_2 = 2;
    public static final int DEVICEID_3 = 3;
    public static final int DEVICEID_ERROR = -1;
    public static final int PINID_4 = 4;
    public static final int PINID_5 = 5;
    OnOffJob tested;

    private ActionDao actionDao;
    private DeviceDao deviceDao;
    private PinController pinController;

    @Before
    public void before() {
        actionDao = mock(ActionDao.class);
        deviceDao = mock(DeviceDao.class);
        pinController = mock(PinController.class);

        tested = new OnOffJob(actionDao, deviceDao, pinController);
    }

    @Test(expected = JobExecutionException.class)
    public void test_execute_catchError() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getJobDetail()).thenThrow(new RuntimeException("Error"));
        tested.execute(context);
    }

    @Test
    public void test_off() throws JobExecutionException {
        testIt(false);
    }

    @Test
    public void test_on() throws JobExecutionException {
        testIt(true);
    }

    public void testIt(boolean on) throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        JobDetail jobDetail = mock(JobDetail.class);
        JobDataMap jobDataMap = mock(JobDataMap.class);

        when(context.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getInt(OnOffJob.ACTION_ID)).thenReturn(ACTION_ID);
        when(jobDataMap.getBoolean(OnOffJob.ON)).thenReturn(on);

        PumpSchedule pumpSchedule = new PumpSchedule();
        pumpSchedule.setDeviceIds(Arrays.asList(new Integer[]{DEVICEID_2, DEVICEID_ERROR, DEVICEID_3}));
        when(actionDao.getAction(ACTION_ID)).thenReturn(pumpSchedule);

        DevicePinSupplierJoin devicePin24 = mock(DevicePinSupplierJoin.class);
        DevicePinSupplierJoin devicePinError = mock(DevicePinSupplierJoin.class);
        DevicePinSupplierJoin devicePin35 = mock(DevicePinSupplierJoin.class);

        when(deviceDao.getPinsForDevice(DEVICEID_2)).thenReturn(Lists.newArrayList(devicePin24));
        when(deviceDao.getPinsForDevice(DEVICEID_ERROR)).thenReturn(Lists.newArrayList(devicePinError));
        when(deviceDao.getPinsForDevice(DEVICEID_3)).thenReturn(Lists.newArrayList(devicePin35));

        doThrow(new RuntimeException("Test Error")).when(pinController).on(devicePinError);
        doThrow(new RuntimeException("Test Error")).when(pinController).off(devicePinError);

        tested.execute(context);

        if (on) {
            verify(pinController).on(devicePin24);
            verify(pinController).on(devicePinError);
            verify(pinController).on(devicePin35);
        }
        else {
            verify(pinController).off(devicePin24);
            verify(pinController).off(devicePinError);
            verify(pinController).off(devicePin35);
        }
    }
}