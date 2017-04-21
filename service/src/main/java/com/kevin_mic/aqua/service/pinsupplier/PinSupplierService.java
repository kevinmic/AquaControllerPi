package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderFactory;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class PinSupplierService {
    private final PinSupplierDao pinSupplierDao;
    private final PinSupplierValidator validator;
    private final PCF8574ProviderFactory pcf8574ProviderFactory;

    @Inject
    public PinSupplierService(PinSupplierDao pinSupplierDao, PinSupplierValidator validator, PCF8574ProviderFactory pcf8574ProviderFactory) {
        this.pinSupplierDao = pinSupplierDao;
        this.validator = validator;
        this.pcf8574ProviderFactory = pcf8574ProviderFactory;
    }

    public PinSupplier addPinSupplier(PinSupplier pinSupplier) {
        pinSupplier.setPinSupplierId(-1);
        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier.getPinSupplierId(), pinSupplier.getHardwareId());
        validator.validateHardwareConnected(pinSupplier);

        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());
        return pinSupplierDao.addPinSupplier(pinSupplier);
    }

    public PinSupplier updatePinSupplier(int supplierId, PinSupplierUpdate update) {
        PinSupplier pinSupplier = getPinSupplier(supplierId);
        String oldHardwareId = pinSupplier.getHardwareId();

        pinSupplier.setName(update.getName());
        pinSupplier.setHardwareId(update.getHardwareId());

        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier.getPinSupplierId(), update.getHardwareId());
        validator.validateHardwareConnected(pinSupplier);

        if (!Objects.equals(oldHardwareId, update.getHardwareId())) {
            shutdownSupplierBus(pinSupplier.getType(), oldHardwareId);
        }

        return pinSupplierDao.updatePinSupplier(pinSupplier);
    }

    public List<PinSupplier> listPinSuppliers() {
        return pinSupplierDao.getPinSuppliers();
    }

    public List<Pin> listPins(int supplierId) {
        return pinSupplierDao.getPins(supplierId);
    }

    public PinSupplier getPinSupplier(int supplierId) {
        return pinSupplierDao.getPinSupplier(supplierId);
    }

    public void deletePinSupplier(int pinSupplierId) {
        try {
            PinSupplier pinSupplier = pinSupplierDao.getPinSupplier(pinSupplierId);
            validator.validatePinsNotOwned(pinSupplierId);

            shutdownSupplierBus(pinSupplier.getType(), pinSupplier.getHardwareId());
            pinSupplierDao.deletePinSupplier(pinSupplierId);
        }
        catch (EntityNotFoundException e) {

        }
    }

    private void shutdownSupplierBus(PinSupplierType type, String hardwareId) {
        if (!StringUtils.isEmpty(hardwareId) && (type == PinSupplierType.PCF8574 || type == PinSupplierType.PCF8574A)) {
            pcf8574ProviderFactory.shutdownBus(type, hardwareId);
        }
    }

}
