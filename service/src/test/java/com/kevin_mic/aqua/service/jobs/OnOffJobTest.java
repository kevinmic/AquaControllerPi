package com.kevin_mic.aqua.service.jobs;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnOffJobTest {
    public static final int ACTION_ID = 1;
    public static final int DEVICEID_2 = 2;
    public static final int DEVICEID_3 = 3;
    public static final int PINID_4 = 4;
    public static final int PINID_5 = 5;
    OnOffJob tested;

    private ActionDao actionDao;
    private DeviceDao deviceDao;
    private GpioController gpioController;

    @Before
    public void before() {
        actionDao = mock(ActionDao.class);
        deviceDao = mock(DeviceDao.class);
        gpioController = mock(GpioController.class);

        tested = new OnOffJob(actionDao, deviceDao, gpioController);
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

        GpioPinDigitalOutput pin4 = mock(GpioPinDigitalOutput.class);
        GpioPinDigitalOutput pin5 = mock(GpioPinDigitalOutput.class);

        when(context.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getInt(OnOffJob.ACTION_ID)).thenReturn(ACTION_ID);
        when(jobDataMap.getBoolean(OnOffJob.ON)).thenReturn(on);

        PumpSchedule pumpSchedule = new PumpSchedule();
        pumpSchedule.setDeviceIds(Arrays.asList(new Integer[]{DEVICEID_2, DEVICEID_3}));
        when(actionDao.getAction(ACTION_ID)).thenReturn(pumpSchedule);

        DevicePinSupplierJoin devicePin24 = new DevicePinSupplierJoin();
        devicePin24.setPinId(PINID_4);
        devicePin24.setType(PinSupplierType.RASBERRY_PI);

        DevicePinSupplierJoin devicePin35 = new DevicePinSupplierJoin();
        devicePin35.setPinId(PINID_5);
        devicePin35.setType(PinSupplierType.PCF8574);

        when(deviceDao.getPinsForDevice(DEVICEID_2)).thenReturn(Arrays.asList(new DevicePinSupplierJoin[] {devicePin24}));
        when(deviceDao.getPinsForDevice(DEVICEID_3)).thenReturn(Arrays.asList(new DevicePinSupplierJoin[] {devicePin35}));

        when(gpioController.getProvisionedPin("pin_" + PINID_4)).thenReturn(pin4);
        when(gpioController.getProvisionedPin("pin_" + PINID_5)).thenReturn(pin5);

        tested.execute(context);

        if (on) {
            verify(pin4).high();
            verify(pin5).low();
        }
        else {
            verify(pin4).low();
            verify(pin5).high();
        }
    }
}