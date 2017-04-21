package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionServiceTest {
    private ActionService tested;
    private ActionDao actionDao;
    private ActionValidator actionValidator;
    private ScheduleServiceFactory scheduleServiceFactory;

    @Before
    public void before() {
        actionDao = mock(ActionDao.class);
        actionValidator = mock(ActionValidator.class);
        scheduleServiceFactory = mock(ScheduleServiceFactory.class);

        tested = new ActionService(actionDao, actionValidator, scheduleServiceFactory);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(actionDao, actionValidator, scheduleServiceFactory);
    }

    @Test
    public void test_add() {
        PumpSchedule action = new PumpSchedule();
        action.setActionId(5);
        when(actionDao.addAction(action)).thenReturn(action);
        assertNotNull(tested.addAction(action));

        verify(actionValidator).validateDevices(action);
        verify(actionValidator).validate(action);
        verify(actionValidator).validateRequired(action);
        verify(scheduleServiceFactory).validateSchedules(action);
        verify(actionDao).addAction(action);
        verify(scheduleServiceFactory).updateSchedules(null, action);
    }

    @Test
    public void test_update() {
        PumpSchedule foundAction = new PumpSchedule();
        PumpSchedule action = new PumpSchedule();
        action.setActionId(5);
        when(actionDao.getAction(5)).thenReturn(foundAction);
        when(actionDao.updateAction(action)).thenReturn(action);
        assertNotNull(tested.updateAction(5, action));

        verify(actionValidator).validateNotChanged(foundAction, action);
        verify(actionValidator).validateDevices(action);
        verify(actionValidator).validate(action);
        verify(actionValidator).validateRequired(action);
        verify(scheduleServiceFactory).validateSchedules(action);
        verify(actionDao).updateAction(action);
        verify(actionDao).getAction(5);
        verify(scheduleServiceFactory).updateSchedules(foundAction, action);
    }

    @Test
    public void test_list() {
        List<ActionInterface> actions = new ArrayList<>();
        actions.add(new PumpSchedule());
        when(actionDao.getActions()).thenReturn(actions);

        List<ActionInterface> actionInterfaces = tested.listActions();
        assertEquals(actions, actionInterfaces);

        verify(actionDao).getActions();
    }

    @Test
    public void test_getAction() {
        PumpSchedule action= new PumpSchedule();
        when(actionDao.getAction(5)).thenReturn(action);

        assertEquals(action, actionDao.getAction(5));

        verify(actionDao).getAction(5);
    }

    @Test
    public void test_delete() {
        tested.deleteAction(5);
        verify(actionDao).deleteAction(5);
        verify(scheduleServiceFactory).deleteSchedules(5);
    }

    @Test
    public void test_findActionsByScheduleType() {
        tested.findActionsByScheduleType(ScheduleType.OnOff);
        verify(actionDao).findActionsByScheduleType(ScheduleType.OnOff);
    }

}