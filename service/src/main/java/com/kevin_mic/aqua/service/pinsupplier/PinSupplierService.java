package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;

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

    public PinSupplier add(PinSupplier pinSupplier) {
        pinSupplier.setPinSupplierId(-1);
        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier.getPinSupplierId(), pinSupplier.getHardwareId());
        validator.validateHardwareConnected(pinSupplier);

        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());
        return pinSupplierDao.insertSupplier(pinSupplier);
    }

    public PinSupplier update(int supplierId, PinSupplierUpdate update) {
        PinSupplier pinSupplier = findById(supplierId);

        pinSupplier.setName(update.getName());
        pinSupplier.setHardwareId(update.getHardwareId());

        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier.getPinSupplierId(), pinSupplier.getHardwareId());
        validator.validateHardwareConnected(pinSupplier);

        return pinSupplierDao.update(pinSupplier);
    }

    public List<PinSupplier> list() {
        return pinSupplierDao.getPinSuppliers();
    }

    public List<Pin> listPins(int supplierId) {
        return pinSupplierDao.getPins(supplierId);
    }

    public PinSupplier findById(int supplierId) {
        return pinSupplierDao.getSupplier(supplierId);
    }

    public void delete(int pinSupplierId) {
        validator.validatePinsNotOwned(pinSupplierId);
        pinSupplierDao.deleteSupplier(pinSupplierId);
    }

}
