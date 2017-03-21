package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.dbi.PinDbi;
import com.kevin_mic.aqua.dbi.PinSupplierDbi;
import com.kevin_mic.aqua.entity.Pin;
import com.kevin_mic.aqua.entity.PinSupplier;
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

    public void insertSupplier(PinSupplier pinSupplier, List<String> pinIds) {
        dbi.inTransaction((handle,transactionStatus) -> {
            PinDbi pinDbi = handle.attach(PinDbi.class);
            PinSupplierDbi pinSupplierDbi = handle.attach(PinSupplierDbi.class);

            pinSupplierDbi.insert(pinSupplier);
            for (String pinId : pinIds) {
                Pin pin = new Pin();
                pin.setPinId(pinId);
                pin.setPinSupplierId(pinSupplier.getPinSupplierId());
                pinDbi.insert(pin);
            }
            return null;
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
        return getPinSupplierDbi().getSupplier(pinSupplierId);
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
}