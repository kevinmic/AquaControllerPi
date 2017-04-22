package com.kevin_mic.aqua.service.jobs;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.dao.DeviceDao;
import com.kevin_mic.aqua.model.actions.OnOffInterface;
import com.kevin_mic.aqua.service.gpio.PinController;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

@Slf4j
public class OnOffJob implements Job {
    public static final String ACTION_ID = "actionId";
    public static final String ON = "on";

    private final ActionDao actionDao;
    private final DeviceDao deviceDao;
    private final PinController pinController;

    @Inject
    public OnOffJob(ActionDao actionDao, DeviceDao deviceDao, PinController pinController) {
        this.actionDao = actionDao;
        this.deviceDao = deviceDao;
        this.pinController = pinController;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int actionId = context.getJobDetail().getJobDataMap().getInt(ACTION_ID);
            boolean on = context.getJobDetail().getJobDataMap().getBoolean(ON);
            OnOffInterface action = (OnOffInterface) actionDao.getAction(actionId);

            log.info("Runnong OnJob - action:{}, on:{}", actionId, on);
            // Attempt
            action.getDeviceIds().stream()
                    .map(deviceId -> {
                        return deviceDao.getPinsForDevice(deviceId);
                    })
                    .flatMap(list -> list.stream())
                    .forEach(pin -> {
                        try {
                            if (on) {
                                pinController.on(pin);
                            }
                            else {
                                pinController.off(pin);
                            }
                        }
                        catch(RuntimeException e) {
                            log.error("Error trying to flip pin, pinId:{}", pin.getPinId(), e);
                        }
                    });
        }
        catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

}
