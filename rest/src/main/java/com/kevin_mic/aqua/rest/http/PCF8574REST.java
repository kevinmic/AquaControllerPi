package com.kevin_mic.aqua.rest.http;

import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.types.PinSupplierType;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderService;
import com.kevin_mic.aqua.service.pinsupplier.PinSupplierService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/pcf8574")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PCF8574REST {
    private final PinSupplierService supplierService;
    private final PCF8574ProviderService providerService;

    @Inject
    public PCF8574REST(PinSupplierService supplierService, PCF8574ProviderService providerService) {
        this.supplierService = supplierService;
        this.providerService = providerService;
    }

    @GET
    @Path("/available/{type}")
    public List<Integer> listPins(@PathParam("type") PinSupplierType type) {
        if (type != PinSupplierType.PCF8574 && type != PinSupplierType.PCF8574A) {
            throw new AquaException(ErrorType.InvalidPinSupplierType);
        }

        List<Integer> usedIds = supplierService.listPinSuppliers().stream()
                .filter((p) -> p.getType() == type)
                .map((p) -> Integer.parseInt(p.getHardwareId()))
                .collect(Collectors.toList());

        return providerService.lookupActiveNonExistingProviders(type, usedIds);
    }
}
