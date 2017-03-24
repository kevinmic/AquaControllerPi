package com.kevin_mic.aqua.rest.http;

import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.updates.DeviceUpdate;
import com.kevin_mic.aqua.service.device.DeviceService;

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

@Path("/v1/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceREST {
    private final DeviceService deviceService;

    @Inject
    public DeviceREST(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GET
    public List<Device> listDevices() {
        return deviceService.list();
    }

    @POST
    public Device create(Device device) {
        return deviceService.add(device);
    }

    @GET
    @Path("/{deviceId}")
    public Device getDevice(@PathParam("deviceId") int deviceId) {
        return deviceService.findById(deviceId);
    }

    @PUT
    @Path("/{deviceId}")
    public Device updateDevice(@PathParam("deviceId") int deviceId, DeviceUpdate update) {
        return deviceService.update(deviceId, update);
    }

    @DELETE
    @Path("/{deviceId}")
    public void deleteDevice(@PathParam("deviceId") int deviceId) {
        deviceService.delete(deviceId);
    }
}
