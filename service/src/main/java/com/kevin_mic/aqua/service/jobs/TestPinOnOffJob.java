package com.kevin_mic.aqua.service.jobs;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.model.types.PinType;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleJob;
import com.kevin_mic.aqua.service.gpio.PinController;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
public class TestPinOnOffJob implements Job {
    public static final String PIN_ID = "pinId";
    public static final String SECONDS = "seconds";

    private final PinSupplierDao pinSupplierDao;
    private final PinController pinController;

    @Inject
    public TestPinOnOffJob(PinSupplierDao pinSupplierDao, PinController pinController) {
        this.pinSupplierDao = pinSupplierDao;
        this.pinController = pinController;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int pinId = context.getJobDetail().getJobDataMap().getInt(PIN_ID);
            int seconds = context.getJobDetail().getJobDataMap().getInt(SECONDS);
            if (seconds > 30) {
                seconds = 30;
            }

            log.info("TestPinOnOffJob - pinId:{}, seconds:{}", pinId, seconds);

            DevicePinSupplierJoin pin = constructDevicePinSupplierJoin(pinId);
            try {
                pinController.on(pin);

                Thread.sleep(seconds * 1000);

                pinController.off(pin);
            }
            catch(RuntimeException e) {
                log.error("Error trying to flip pin, pinId:{}", pin.getPinId(), e);
            }
        }
        catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private DevicePinSupplierJoin constructDevicePinSupplierJoin(int pinId) {
        Pin pin = pinSupplierDao.getPin(pinId);
        PinSupplier pinSupplier = pinSupplierDao.getPinSupplier(pin.getPinSupplierId());
        DevicePinSupplierJoin join = new DevicePinSupplierJoin();
        join.setPinId(pin.getPinId());
        join.setPinNumber(pin.getPinNumber());
        join.setPinSupplierHardwareId(pinSupplier.getHardwareId());
        join.setPinSupplierSubType(pinSupplier.getSubType());
        join.setPinSupplierType(pinSupplier.getType());
        join.setPinSupplierId(pinSupplier.getPinSupplierId());
        join.setPinType(PinType.Toggle);
        join.setDeviceId(-1);
        return join;
    }

    public static ScheduleJob getScheduleJob(int pinId, Integer seconds) {
        if (seconds == null) {
            seconds = 1;
        }

        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setJobDetail( newJob(TestPinOnOffJob.class)
                .withIdentity("TEST_IMMEDIATE_" + pinId)
                .usingJobData(PIN_ID, pinId)
                .usingJobData(SECONDS, seconds)
                .build()
        );

        scheduleJob.setTriggers(Lists.newArrayList(
                newTrigger()
                        .withIdentity("TEST_IMMEDIATE_" + pinId)
                        .startNow()
                        .build()
        ));

        return scheduleJob;
    }
}
