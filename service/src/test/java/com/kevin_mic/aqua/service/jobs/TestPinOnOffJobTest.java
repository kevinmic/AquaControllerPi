package com.kevin_mic.aqua.service.jobs;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleJob;
import com.kevin_mic.aqua.service.gpio.PinController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestPinOnOffJobTest {
    public static final int PIN_ID = 3;
    public static final int PIN_SUPPLIER_ID = 2;
    TestPinOnOffJob tested;
    PinSupplierDao pinSupplierDao;
    PinController pinController;

    @Before
    public void before() {
        pinSupplierDao = mock(PinSupplierDao.class);
        pinController = mock(PinController.class);

        tested = new TestPinOnOffJob(pinSupplierDao, pinController);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(pinController, pinSupplierDao);
    }

    @Test(expected = JobExecutionException.class)
    public void test_execute_catchError() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getJobDetail()).thenThrow(new RuntimeException("Error"));
        tested.execute(context);
    }

    @Test
    public void test_execute() throws JobExecutionException {
        ScheduleJob scheduleJob = TestPinOnOffJob.getScheduleJob(PIN_ID, 1);
        JobExecutionContext context = mock(JobExecutionContext.class);

        Pin pin = createPin();
        PinSupplier pinSupplier = createPinSupplier();

        when(context.getJobDetail()).thenReturn(scheduleJob.getJobDetail());
        when(pinSupplierDao.getPin(PIN_ID)).thenReturn(pin);
        when(pinSupplierDao.getPinSupplier(PIN_SUPPLIER_ID)).thenReturn(pinSupplier);

        ArgumentCaptor<DevicePinSupplierJoin> captor = ArgumentCaptor.forClass(DevicePinSupplierJoin.class);
        tested.execute(context);

        verify(pinSupplierDao).getPin(PIN_ID);
        verify(pinSupplierDao).getPinSupplier(PIN_SUPPLIER_ID);
        verify(pinController).on(captor.capture());
        verify(pinController).off(captor.capture());

        List<DevicePinSupplierJoin> allValues = captor.getAllValues();
        assertEquals(2, allValues.size());
        assertEquals(allValues.get(0), allValues.get(1));

        verifyDeviceSupplierPinJoin(pin, pinSupplier, allValues);
    }

    private PinSupplier createPinSupplier() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setSubType(PinSupplierSubType.SensorArray);
        pinSupplier.setType(PinSupplierType.PCF8574);
        pinSupplier.setPinSupplierId(PIN_SUPPLIER_ID);
        pinSupplier.setHardwareId("HRD");
        pinSupplier.setName("NAME");
        return pinSupplier;
    }

    private Pin createPin() {
        Pin pin = new Pin();
        pin.setOwnedByDeviceId(5);
        pin.setPinSupplierId(PIN_SUPPLIER_ID);
        pin.setPinId(PIN_ID);
        pin.setPinNumber(3);
        return pin;
    }

    private void verifyDeviceSupplierPinJoin(Pin pin, PinSupplier pinSupplier, List<DevicePinSupplierJoin> allValues) {
        DevicePinSupplierJoin devicePinSupplierJoin = allValues.get(0);
        assertEquals(pin.getPinId(), devicePinSupplierJoin.getPinId());
        assertEquals(pin.getPinNumber(), devicePinSupplierJoin.getPinNumber());
        assertEquals(pinSupplier.getPinSupplierId(), devicePinSupplierJoin.getPinSupplierId());
        assertEquals(pinSupplier.getHardwareId(), devicePinSupplierJoin.getPinSupplierHardwareId());
        assertEquals(pinSupplier.getType(), devicePinSupplierJoin.getPinSupplierType());
        assertEquals(pinSupplier.getSubType(), devicePinSupplierJoin.getPinSupplierSubType());
    }

    @Test
    public void test_getScheduleJob() {
        ScheduleJob scheduleJob = TestPinOnOffJob.getScheduleJob(PIN_ID, 5);
        assertNotNull(scheduleJob);
        assertNotNull(scheduleJob.getJobDetail());
        assertNotNull(scheduleJob.getTriggers());
        assertEquals(1, scheduleJob.getTriggers().size());

        JobDetail jobDetail = scheduleJob.getJobDetail();
        assertEquals(jobDetail.getJobDataMap().getInt(TestPinOnOffJob.PIN_ID), PIN_ID);
        assertEquals(jobDetail.getJobDataMap().getInt(TestPinOnOffJob.SECONDS), 5);
    }
}