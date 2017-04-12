package com.kevin_mic.aqua.rest;

import com.kevin_mic.aqua.service.AquaException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

public class AquaExceptionMapper implements ExceptionMapper<AquaException> {
    @Override
    public Response toResponse(AquaException e) {
        Map<String,String> json = new HashMap<>();
        json.put("errortype", e.getMessage());
        return Response.status(400).entity(json).build();
    }
}
