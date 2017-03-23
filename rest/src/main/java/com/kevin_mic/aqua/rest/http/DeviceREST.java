package com.kevin_mic.aqua.rest.http;

import com.kevin_mic.aqua.model.Device;
import com.kevin_mic.aqua.service.device.DeviceService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
}
