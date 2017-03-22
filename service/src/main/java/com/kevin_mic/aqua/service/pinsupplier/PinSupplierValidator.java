package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;

import javax.inject.Inject;

public class PinSupplierValidator {
    PinSupplierDao pinSupplierDao;

    @Inject
    PinSupplierValidator(PinSupplierDao pinSupplierDao) {
        this.pinSupplierDao = pinSupplierDao;
    }

    void validate(PinSupplier pinSupplier) {
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

    void validateHardwareConnected(PinSupplier pinSupplier) {
        // TODO
    }

    void validateHardwareIdNotUsed(PinSupplier pinSupplier) {
        if (pinSupplierDao.findByHardwareId(pinSupplier.getHardwareId()) != null) {
            throw new AquaException(ErrorType.SupplierHardwareIdAlreadyUsed);
        }
    }

}