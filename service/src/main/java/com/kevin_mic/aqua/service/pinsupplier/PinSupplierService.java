package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;

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
        validator.validate(pinSupplier);
        validator.validateHardwareIdNotUsed(pinSupplier);
        validator.validateHardwareConnected(pinSupplier);

        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());
        pinSupplierDao.insertSupplier(pinSupplier);

        return pinSupplier;
    }

    public List<PinSupplier> list() {
        return pinSupplierDao.getPinSuppliers();
    }

    public List<Pin> listPins(int supplierId) {
        return pinSupplierDao.getPins(supplierId);
    }
}
