package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderService;
import com.kevin_mic.aqua.service.jobs.TestPinOnOffJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PinSupplierServiceTest {
    public static final String HARDWARE_ID = "HARDWARE_ID";
    public static final String HARDWARE_ID_2 = "HARDWARE_ID_2";
    public static final String NAME = "NAME";
    PinSupplierService tested;
    PinSupplierDao supplierDao;
    PinSupplierValidator validator;
    PCF8574ProviderService pcf8574ProviderService;
    ScheduleServiceFactory scheduler;

    @Before
    public void before() {
        supplierDao = mock(PinSupplierDao.class);
        validator = mock(PinSupplierValidator.class);
        pcf8574ProviderService = mock(PCF8574ProviderService.class);
        scheduler = mock(ScheduleServiceFactory.class);

        tested = new PinSupplierService(supplierDao, validator, pcf8574ProviderService, scheduler);
    }

    @After
    public void after() {
        Mockito.verifyNoMoreInteractions(supplierDao, validator, scheduler, pcf8574ProviderService);
    }

    @Test
    public void test_add() {
        PinSupplier supplier = new PinSupplier();
        supplier.setPinSupplierId(55);
        supplier.setHardwareId(HARDWARE_ID);

        when(supplierDao.addPinSupplier(supplier)).thenReturn(supplier);
        assertNotNull(tested.addPinSupplier(supplier));

        verify(validator).validate(supplier);
        verify(validator).validateHardwareConnected(supplier);
        verify(validator).validateHardwareIdNotUsed(-1, HARDWARE_ID);
        verify(supplierDao).getNextId();

        verify(supplierDao).addPinSupplier(supplier);
    }

    @Test
    public void test_update() {
        PinSupplier pinSupplier = mock(PinSupplier.class);

        PinSupplierUpdate update = new PinSupplierUpdate();
        update.setHardwareId(HARDWARE_ID);
        update.setName(NAME);

        when(supplierDao.getPinSupplier(55)).thenReturn(pinSupplier);
        when(pinSupplier.getPinSupplierId()).thenReturn(55);
        when(pinSupplier.getHardwareId()).thenReturn(HARDWARE_ID_2);
        when(pinSupplier.getType()).thenReturn(PinSupplierType.PCF8574);

        when(supplierDao.updatePinSupplier(pinSupplier)).thenReturn(pinSupplier);
        assertNotNull(tested.updatePinSupplier(55, update));

        verify(pinSupplier).setName(NAME);
        verify(pinSupplier).setHardwareId(HARDWARE_ID);
        verify(validator).validate(pinSupplier);
        verify(validator).validateHardwareConnected(pinSupplier);
        verify(validator).validateHardwareIdNotUsed(55, HARDWARE_ID);
        verify(pcf8574ProviderService).shutdownBus(PinSupplierType.PCF8574, HARDWARE_ID_2);

        verify(supplierDao).getPinSupplier(55);
        verify(supplierDao).updatePinSupplier(pinSupplier);
    }

    @Test
    public void test_findById() {
        when(supplierDao.getPinSupplier(1)).thenReturn(new PinSupplier());
        assertNotNull(tested.getPinSupplier(1));

        verify(supplierDao).getPinSupplier(1);
    }

    @Test
    public void test_list() {
        when(supplierDao.getPinSuppliers()).thenReturn(new ArrayList<PinSupplier>());
        assertNotNull(tested.listPinSuppliers());

        verify(supplierDao).getPinSuppliers();
    }

    @Test
    public void test_listPins() {
        when(supplierDao.getPins(1)).thenReturn(new ArrayList<Pin>());
        assertNotNull(tested.listPins(1));

        verify(supplierDao).getPins(1);
    }

    @Test
    public void test_delete_PCF8574() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("123");
        pinSupplier.setType(PinSupplierType.PCF8574);

        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);
        tested.deletePinSupplier(1);
        verify(supplierDao).getPinSupplier(1);
        verify(validator).validatePinsNotOwned(1);
        verify(supplierDao).deletePinSupplier(1);
        verify(pcf8574ProviderService).shutdownBus(PinSupplierType.PCF8574, "123");
    }

    @Test
    public void test_delete_PCF8574A() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("123");
        pinSupplier.setType(PinSupplierType.PCF8574A);

        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);
        tested.deletePinSupplier(1);
        verify(supplierDao).getPinSupplier(1);
        verify(validator).validatePinsNotOwned(1);
        verify(supplierDao).deletePinSupplier(1);
        verify(pcf8574ProviderService).shutdownBus(PinSupplierType.PCF8574A, "123");
    }

    @Test
    public void test_delete_other() {
        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setHardwareId("123");
        pinSupplier.setType(PinSupplierType.RASBERRY_PI);

        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);
        tested.deletePinSupplier(1);
        verify(supplierDao).getPinSupplier(1);
        verify(validator).validatePinsNotOwned(1);
        verify(supplierDao).deletePinSupplier(1);
    }

    @Test
    public void test_delete_entityNotFound() {
        when(supplierDao.getPinSupplier(1)).thenThrow(new EntityNotFoundException("", 1));
        tested.deletePinSupplier(1);
        verify(supplierDao).getPinSupplier(1);
    }

    @Test
    public void test_testPin_ownedByDevice() {
        Pin pin = new Pin();
        pin.setPinSupplierId(1);
        pin.setOwnedByDeviceId(5);

        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setType(PinSupplierType.PCF8574);
        pinSupplier.setSubType(PinSupplierSubType.Relay_120_VAC);

        when(supplierDao.getPin(2)).thenReturn(pin);
        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);

        assertThatThrownBy(() -> tested.testPin(2, 5)).hasMessage(ErrorType.PinAlreadyOwned.name());
    }

    @Test
    public void test_testPin_relay_120V() {
        Pin pin = new Pin();
        pin.setPinSupplierId(1);

        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setType(PinSupplierType.PCF8574);
        pinSupplier.setSubType(PinSupplierSubType.Relay_120_VAC);

        when(supplierDao.getPin(2)).thenReturn(pin);
        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);
        tested.testPin(2, 5);

        verify(scheduler).scheduleJob(TestPinOnOffJob.getScheduleJob(2, 5));
        verify(supplierDao).getPinSupplier(1);
        verify(supplierDao).getPin(2);
    }

    @Test
    public void test_testPin_relay_12V() {
        Pin pin = new Pin();
        pin.setPinSupplierId(1);

        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setType(PinSupplierType.PCF8574);
        pinSupplier.setSubType(PinSupplierSubType.Relay_12_VDC);

        when(supplierDao.getPin(2)).thenReturn(pin);
        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);
        tested.testPin(2, 5);

        verify(scheduler).scheduleJob(TestPinOnOffJob.getScheduleJob(2, 5));
        verify(supplierDao).getPinSupplier(1);
        verify(supplierDao).getPin(2);
    }

    @Test
    public void test_testPin_other() {
        Pin pin = new Pin();
        pin.setPinSupplierId(1);

        PinSupplier pinSupplier = new PinSupplier();
        pinSupplier.setType(PinSupplierType.PCF8574);

        when(supplierDao.getPin(2)).thenReturn(pin);
        when(supplierDao.getPinSupplier(1)).thenReturn(pinSupplier);

        pinSupplier.setSubType(PinSupplierSubType.PI);
        assertThatThrownBy(() -> tested.testPin(2, 5)).hasMessage(ErrorType.PinSupplierSubTypeTestNotImplemented.name());
        pinSupplier.setSubType(PinSupplierSubType.SensorArray);
        assertThatThrownBy(() -> tested.testPin(2, 5)).hasMessage(ErrorType.PinSupplierSubTypeTestNotImplemented.name());
        pinSupplier.setSubType(PinSupplierSubType.StepperArray);
        assertThatThrownBy(() -> tested.testPin(2, 5)).hasMessage(ErrorType.PinSupplierSubTypeTestNotImplemented.name());

        Mockito.reset(supplierDao);
    }

}
