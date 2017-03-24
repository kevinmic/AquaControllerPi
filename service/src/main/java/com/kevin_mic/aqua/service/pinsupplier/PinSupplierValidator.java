package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierSubType;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

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
        if (StringUtils.isBlank(pinSupplier.getName())) {
            throw new AquaException(ErrorType.SupplierNameCannotBeNull);
        }
        if (StringUtils.isBlank(pinSupplier.getHardwareId())) {
            throw new AquaException(ErrorType.SupplierHardwareIdCannotBeNull);
        }
        if (pinSupplier.getType() == null) {
            throw new AquaException(ErrorType.SupplierTypeCannotBeNull);
        }
        if (pinSupplier.getSubType() == null) {
            throw new AquaException(ErrorType.SupplierSubTypeCannotBeNull);
        }
        if (!pinSupplier.getType().isSubTypeAllowed(pinSupplier.getSubType())) {
            throw new AquaException(ErrorType.SupplierSubTypeIncompatibleWithType);
        }
    }

    void validateHardwareConnected(PinSupplier pinSupplier) {
        // TODO
    }

    void validateHardwareIdNotUsed(int pinSupplierId, String hardwareId) {
        PinSupplier byHardwareId = pinSupplierDao.findByHardwareId(hardwareId);
        if (byHardwareId != null && byHardwareId.getPinSupplierId() != pinSupplierId) {
            throw new AquaException(ErrorType.SupplierHardwareIdAlreadyUsed);
        }
    }

    public void validatePinsNotOwned(int pinSupplierId) {
        List<Pin> pins = pinSupplierDao.getPins(pinSupplierId);
        if (pins.stream().filter(p -> p.getOwnedByDeviceId() != null).count() > 0) {
            throw new AquaException(ErrorType.PinsInUse);
        }
    }
}
