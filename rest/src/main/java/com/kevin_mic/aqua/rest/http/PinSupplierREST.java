package com.kevin_mic.aqua.rest.http;

import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import com.kevin_mic.aqua.model.updates.PinSupplierUpdate;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
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

@Path("/v1/pinsuppliers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PinSupplierREST {
    private final PinSupplierService supplierService;

    @Inject
    public PinSupplierREST(PinSupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GET
    public List<PinSupplier> listPins() {
        return supplierService.list();
    }

    @POST
    public PinSupplier add(PinSupplier supplier) {
        return supplierService.add(supplier);
    }

    @GET
    @Path("/{supplierId}")
    public PinSupplier getPinSupplier(@PathParam("supplierId") int supplierId) {
        return supplierService.findById(supplierId);
    }

    @PUT
    @Path("/{supplierId}")
    public PinSupplier updatePinSupplier(@PathParam("supplierId") int supplierId, PinSupplierUpdate update) {
        return supplierService.update(supplierId, update);
    }

    @DELETE
    @Path("/{supplierId}")
    public void deletePinSupplier(@PathParam("supplierId") int supplierId) {
        supplierService.delete(supplierId);
    }

    @GET
    @Path("/{supplierId}/pins")
    public List<Pin> getPins(@PathParam("supplierId") int supplierId) {
        return supplierService.listPins(supplierId);
    }
}
