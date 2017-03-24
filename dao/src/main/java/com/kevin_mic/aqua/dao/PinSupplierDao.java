package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.dbi.PinDbi;
import com.kevin_mic.aqua.dbi.PinSupplierDbi;
import com.kevin_mic.aqua.model.EntityNotFoundException;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import org.skife.jdbi.v2.DBI;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class PinSupplierDao {
    private DBI dbi;

    @Inject
    public PinSupplierDao(DBI dbi) {
        this.dbi = dbi;
    }

    public PinSupplier insertSupplier(PinSupplier pinSupplier) {
        return dbi.inTransaction((handle,transactionStatus) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            PinSupplierDbi pinSupplierDbi = handle.attach(PinSupplierDbi.class);

            pinSupplierDbi.insert(pinSupplier);
            for (int pinNumber = 0; pinNumber < pinSupplier.getType().getNumberOfPins(); pinNumber++) {
                Pin pin = new Pin();
                pin.setPinNumber(pinNumber);
                pin.setPinSupplierId(pinSupplier.getPinSupplierId());
                pinDbi.insert(pin);
            }
            return pinSupplier;
        });
    }

    public void deleteSupplier(int pinSupplierId) {
        dbi.inTransaction((handle,ts) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            PinSupplierDbi pinSupplierDbi = handle.attach(PinSupplierDbi.class);

            List<Pin> usedPins = pinDbi.getPinsForSupplier(pinSupplierId)
                    .stream()
                    .filter(pin -> pin.getOwnedByDeviceId() != null)
                    .collect(Collectors.toList());

            if (!usedPins.isEmpty()) {
                throw new RuntimeException("Some pins are still in use");
            }

            pinDbi.deleteBySupplier(pinSupplierId);
            pinSupplierDbi.delete(pinSupplierId);

            return null;
        });
    }

    public PinSupplier getSupplier(int pinSupplierId) {
        PinSupplier supplier = getPinSupplierDbi().getSupplier(pinSupplierId);
        if (supplier == null) {
            throw new EntityNotFoundException("PinSupplier", pinSupplierId);
        }
        return supplier;
    }

    public List<Pin> getPins(int pinSupplierId) {
        return getPinDbi().getPinsForSupplier(pinSupplierId);
    }

    public PinSupplier findByHardwareId(String hardwareId) {
        return getPinSupplierDbi().getSupplierByHardwareId(hardwareId);
    }

    private PinDbi getPinDbi() {
        return dbi.onDemand(PinDbi.class);
    }

    private PinSupplierDbi getPinSupplierDbi() {
        return dbi.onDemand((PinSupplierDbi.class));
    }

    public int getNextId() {
        return getPinSupplierDbi().getNextId();
    }

    public List<PinSupplier> getPinSuppliers() {
        return getPinSupplierDbi().getSuppliers();
    }

    public Pin findPin(int pinId) {
        Pin pin = getPinSupplierDbi().getPin(pinId);
        if (pin == null) {
            throw new EntityNotFoundException("Pin", pinId);
        }
        return pin;
    }

    public PinSupplier update(PinSupplier pinSupplier) {
        getPinSupplierDbi().update(pinSupplier);
        return getSupplier(pinSupplier.getPinSupplierId());
    }
}
