package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;

import javax.inject.Inject;
import java.util.List;

public class PinSupplierService {
    private final PinSupplierDao pinSupplierDao;
    private final PinSupplierValidator validator;

    @Inject
    public PinSupplierService(PinSupplierDao pinSupplierDao, PinSupplierValidator validator) {
        this.pinSupplierDao = pinSupplierDao;
        this.validator = validator;
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

        pinSupplier.setName(update.getName());
        pinSupplier.setHardwareId(update.getHardwareId());

        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier.getPinSupplierId(), pinSupplier.getHardwareId());
        validator.validateHardwareConnected(pinSupplier);

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
        validator.validatePinsNotOwned(pinSupplierId);
        pinSupplierDao.deletePinSupplier(pinSupplierId);
    }

}
