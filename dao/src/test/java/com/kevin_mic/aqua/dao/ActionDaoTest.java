package com.kevin_mic.aqua.dao;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.actions.TopOff;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.ScheduleType;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ActionDaoTest extends BaseTest {
    ActionDao tested;
    DeviceDao deviceDao;

    @Before
    public void beforeMethod() {
        tested = new ActionDao(dbi, mapper);
        deviceDao = new DeviceDao(dbi);
    }

    @Test
    public void test_crud() {
        int deviceId = createDevice();

        TopOff topOff = new TopOff();
        topOff.setActionId(5);
        topOff.setName("NAME");
        topOff.setMaxRunTimeSeconds(6);
        topOff.setPumpId(deviceId);
        topOff.setRefillReserviorFloat(8);
        topOff.setTankWaterLevelFloat(9);
        topOff.setSchedule(new AlwaysOnSchedule());

        tested.addAction(topOff);

        assertNotEquals(5, topOff.getActionId());

        ActionInterface foundAction = tested.getAction(topOff.getActionId());
        assertEquals(foundAction, topOff);

        assertEquals(1, tested.getActions().size());

        topOff.setTankWaterLevelFloat(100);
        topOff.setName("NAME_2");
        tested.updateAction(topOff);

        foundAction = tested.getAction(topOff.getActionId());
        assertEquals(foundAction, topOff);

        List<ActionInterface> actions = tested.findActionsByScheduleType(ScheduleType.AlwaysOn);
        assertEquals(1, actions.size());

        tested.deleteAction(topOff.getActionId());

        assertEquals(0, tested.getActions().size());

        try {
            tested.getAction(topOff.getActionId());
            fail();
        }
        catch (EntityNotFoundException e) {
        }
    }

    @Test
    public void test_multiDeviceIds() {
        int device = createDevice();

        PumpSchedule pumpSchedule = new PumpSchedule();
        pumpSchedule.setName("NAME");
        ArrayList<Integer> pumpIds = new ArrayList<>();
        pumpIds.add(device);
        pumpSchedule.setDeviceIds(pumpIds);

        tested.addAction(pumpSchedule);
    }

    @Test
    public void test_deviceMustExist() {
        TopOff topOff = new TopOff();
        topOff.setName("NAME");
        topOff.setPumpId(15);

        try {
            tested.addAction(topOff);
            fail();
        }
        catch (CallbackFailedException e) {
            // expected
        }
    }

    private int createDevice() {
        Device device = new Device();
        device.setName("NAME");
        device.setType(DeviceType.DosingPumpPeristalticStepper);
        deviceDao.addDevice(device);
        return device.getDeviceId();
    }
}