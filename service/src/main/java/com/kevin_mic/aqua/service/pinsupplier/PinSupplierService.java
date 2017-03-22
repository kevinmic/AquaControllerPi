package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.entity.PinSupplier;

import javax.inject.Inject;

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

}
