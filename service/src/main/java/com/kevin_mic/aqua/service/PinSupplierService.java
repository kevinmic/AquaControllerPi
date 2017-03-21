package com.kevin_mic.aqua.service;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.entity.PinSupplier;

import javax.inject.Inject;
import java.util.List;

public class PinSupplierService {
    PinSupplierDao pinSupplierDao;

    @Inject
    public PinSupplierService(PinSupplierDao pinSupplierDao) {
        this.pinSupplierDao = pinSupplierDao;
    }

    public PinSupplier add(PinSupplier pinSupplier) {
        validate(pinSupplier);
        validateHardwareIdNotUsed(pinSupplier.getHardwareId());
        pinSupplier.setPinSupplierId(pinSupplierDao.getNextId());

        // TODO, we need to get a pinSupplierId first
        List<String> pins = pinSupplier.getSupplierType().getPinIds(pinSupplier.getPinSupplierId(), pinSupplier.getHardwareId());
        pinSupplierDao.insertSupplier(pinSupplier, pins);

        return pinSupplier;
    }

    private void generateAndValidateId(PinSupplier pinSupplier) {
        pinSupplier.getSupplierType();
    }

    private void validateHardwareIdNotUsed(String hardwareId) {
        if (pinSupplierDao.findByHardwareId(hardwareId) != null) {
            throw new AquaException(ErrorType.SupplierHardwareIdAlreadyUsed);
        }
    }

    private void validate(PinSupplier pinSupplier) {
        if (pinSupplier == null) {
            throw new AquaException(ErrorType.SupplierCannotBeNull);
        }
        if (pinSupplier.getName() == null) {
            throw new AquaException(ErrorType.SupplierNameCannotBeNull);
        }
        if (pinSupplier.getHardwareId() == null) {
            throw new AquaException(ErrorType.SupplierHardwareIdCannotBeNull);
        }
        if (pinSupplier.getSupplierType() == null) {
            throw new AquaException(ErrorType.SupplierTypeCannotBeNull);
        }
    }
}
