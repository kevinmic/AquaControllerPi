package com.kevin_mic.aqua.service.pinsupplier;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

public class PinSupplierValidator {
    private final PinSupplierDao pinSupplierDao;
    private final PCF8574ProviderService pcf8574ProviderService;

    @Inject
    PinSupplierValidator(PinSupplierDao pinSupplierDao, PCF8574ProviderService pcf8574ProviderService) {
        this.pinSupplierDao = pinSupplierDao;
        this.pcf8574ProviderService = pcf8574ProviderService;
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
        if (pinSupplier.getType() == PinSupplierType.PCF8574 || pinSupplier.getType() == PinSupplierType.PCF8574A) {
            pcf8574ProviderService.assertValidHardwareId(pinSupplier.getType(), pinSupplier.getHardwareId());
        }
        if (pinSupplier.getSubType() == null) {
            throw new AquaException(ErrorType.SupplierSubTypeCannotBeNull);
        }
        if (!pinSupplier.getType().isSubTypeAllowed(pinSupplier.getSubType())) {
            throw new AquaException(ErrorType.SupplierSubTypeIncompatibleWithType);
        }
    }

    void validateHardwareConnected(PinSupplier pinSupplier) {
        if (pinSupplier.getType() == PinSupplierType.PCF8574 ||pinSupplier.getType() == PinSupplierType.PCF8574A) {
            if (!pcf8574ProviderService.isDeviceActive(Integer.parseInt(pinSupplier.getHardwareId()))) {
                throw new AquaException(ErrorType.SupplierHardwareIdNotActive);
            }
        }
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
