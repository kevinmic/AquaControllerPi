package com.kevin_mic.aqua.service.jobs;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.actions.OnOffInterface;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import com.kevin_mic.aqua.service.gpio.PinHelper;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.util.List;

public class OnOffJob implements Job {
    public static final String ACTION_ID = "actionId";
    public static final String ON = "on";

    private final ActionDao actionDao;
    private final DeviceDao deviceDao;
    private final GpioController gpioController;

    @Inject
    public OnOffJob(ActionDao actionDao, DeviceDao deviceDao, GpioController gpioController) {
        this.actionDao = actionDao;
        this.deviceDao = deviceDao;
        this.gpioController = gpioController;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int actionId = context.getJobDetail().getJobDataMap().getInt(ACTION_ID);
            boolean on = context.getJobDetail().getJobDataMap().getBoolean(ON);
            OnOffInterface action = (OnOffInterface) actionDao.getAction(actionId);
            List<Integer> deviceIds = action.getDeviceIds();
            for (Integer deviceId : deviceIds) {
                List<DevicePinSupplierJoin> pins = deviceDao.getPinsForDevice(deviceId);
                for (DevicePinSupplierJoin pin : pins) {
                    GpioPinDigitalOutput provisionedPin = (GpioPinDigitalOutput) gpioController.getProvisionedPin("pin_" + pin.getPinId());

                    if (on) {
                        PinHelper.on(pin.getType(), pin.getSubType(), provisionedPin);
                    }
                    else {
                        PinHelper.off(pin.getType(), pin.getSubType(), provisionedPin);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
