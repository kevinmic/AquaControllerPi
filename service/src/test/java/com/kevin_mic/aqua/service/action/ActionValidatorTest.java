package com.kevin_mic.aqua.service.action;

import com.kevin_mic.aqua.dao.ActionDao;
import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.DosingTimed;
import com.kevin_mic.aqua.model.actions.PumpSchedule;
import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.device.DeviceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionValidatorTest {
    private ActionValidator tested;
    private ActionDao actionDao;
    private DeviceService deviceService;

    @Before
    public void before() {
        actionDao = mock(ActionDao.class);
        deviceService = mock(DeviceService.class);

        tested = new ActionValidator(deviceService, actionDao);
    }


    @Test
    public void test_validate_valid() {
        ActionInterface action = getValidAction();
        tested.validate(action);

        Mockito.verifyNoMoreInteractions(actionDao, deviceService);
    }

    @Test
    public void test_validate_invalidName() {
        ActionInterface action = getValidAction();
        action.setName(null);

        assertThatThrownBy(() -> tested.validate(action)).hasMessage(ErrorType.ActionNameRequried.name());
    }

    private ActionInterface getValidAction() {
        PumpSchedule pumpSchedule = new PumpSchedule();
        pumpSchedule.setName("NAME");
        return pumpSchedule;
    }

    @Test
    public void test_validateRequired_emptyList() {
        PumpSchedule action = new PumpSchedule();
        action.setSchedule(new AlwaysOnSchedule());
        action.setPumpIds(new ArrayList<>());

        assertThatThrownBy(() -> tested.validateRequired(action)).hasMessage(ErrorType.ActionFieldCannotBeEmpty + ":pumpIds");
    }

    @Test
    public void test_validateRequired_singular_null() {
        PumpSchedule action = new PumpSchedule();
        action.setSchedule(null);
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        assertThatThrownBy(() -> tested.validateRequired(action)).hasMessage(ErrorType.ActionFieldCannotBeNull + ":schedule");
    }

    @Test
    public void test_validateRequired_list_null() {
        PumpSchedule action = new PumpSchedule();
        action.setSchedule(new AlwaysOnSchedule());
        action.setPumpIds(null);

        assertThatThrownBy(() -> tested.validateRequired(action)).hasMessage(ErrorType.ActionFieldCannotBeNull + ":pumpIds");
    }

    @Test
    public void test_validateRequired_valid() {
        PumpSchedule action = new PumpSchedule();
        action.setSchedule(new AlwaysOnSchedule());
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        tested.validateRequired(action);

        Mockito.verifyNoMoreInteractions(actionDao, deviceService);
    }

    @Test
    public void test_validateDevices_list_invalidDeviceType() {
        PumpSchedule action = new PumpSchedule();
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        Device device = new Device();
        device.setType(DeviceType.FanAC);
        when(deviceService.getDevice(1)).thenReturn(device);

        assertThatThrownBy(() -> tested.validateDevices(action)).hasMessage(ErrorType.InvalidDeviceType + ":pumpIds:1");
    }

    @Test
    public void test_validateDevices_single_invalidDeviceType() {
        DosingTimed action = new DosingTimed();
        action.setPumpId(1);

        Device device = new Device();
        device.setType(DeviceType.FanAC);
        when(deviceService.getDevice(1)).thenReturn(device);

        assertThatThrownBy(() -> tested.validateDevices(action)).hasMessage(ErrorType.InvalidDeviceType + ":pumpId:1");
    }

    @Test
    public void test_validateDevices_deviceAlreadyOwned_ownedByAnotherAction() {
        PumpSchedule action = new PumpSchedule();
        action.setActionId(10);
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        Device device = new Device();
        device.setType(DeviceType.PumpAC);
        when(deviceService.getDevice(1)).thenReturn(device);
        when(actionDao.getActionIdThatOwnsDevice(1)).thenReturn(11);

        assertThatThrownBy(() -> tested.validateDevices(action)).hasMessage(ErrorType.DeviceAlreadyOwnedByAnotherAction + ":pumpIds:1");

        verify(deviceService).getDevice(1);
    }

    @Test
    public void test_validateDevices_deviceAlreadyOwned_ownedByOurAction() {
        PumpSchedule action = new PumpSchedule();
        action.setActionId(10);
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        Device device = new Device();
        device.setType(DeviceType.PumpAC);
        when(deviceService.getDevice(1)).thenReturn(device);
        when(actionDao.getActionIdThatOwnsDevice(1)).thenReturn(10);

        tested.validateDevices(action);

        verify(deviceService).getDevice(1);
        verify(actionDao).getActionIdThatOwnsDevice(1);

        Mockito.verifyNoMoreInteractions(actionDao, deviceService);
    }

    @Test
    public void test_validateDevices_deviceAlreadyOwned_notOwned() {
        PumpSchedule action = new PumpSchedule();
        action.setActionId(10);
        action.setPumpIds(Arrays.asList(new Integer[] {1}));

        Device device = new Device();
        device.setType(DeviceType.PumpAC);
        when(deviceService.getDevice(1)).thenReturn(device);
        when(actionDao.getActionIdThatOwnsDevice(1)).thenReturn(null);

        tested.validateDevices(action);

        verify(deviceService).getDevice(1);
        verify(actionDao).getActionIdThatOwnsDevice(1);

        Mockito.verifyNoMoreInteractions(actionDao, deviceService);
    }

}