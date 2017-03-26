package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionServiceTest {
    private ActionService tested;
    private ActionDao actionDao;
    private ActionValidator actionValidator;

    @Before
    public void before() {
        actionDao = mock(ActionDao.class);
        actionValidator = mock(ActionValidator.class);

        tested = new ActionService(actionDao, actionValidator);
    }

    @Test
    public void test_add() {
        PumpSchedule action = new PumpSchedule();
        when(actionDao.insert(action)).thenReturn(action);
        assertNotNull(tested.addAction(action));

        verify(actionValidator).validateDevices(action);
        verify(actionValidator).validate(action);
        verify(actionValidator).validateRequired(action);
        verify(actionDao).insert(action);
    }

}