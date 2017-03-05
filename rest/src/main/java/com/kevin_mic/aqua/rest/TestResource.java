package com.kevin_mic.aqua.rest;

import com.kevin_mic.aqua.model.TestObj;
import com.kevin_mic.aqua.service.TestService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
    TestService testService;

    @Inject
    public TestResource(TestService testService) {
        this.testService = testService;
    }

    @GET
    public List<TestObj> testGet() {
        return testService.findTests();
    }

    @POST
    public void addTest(TestObj obj) {
        testService.insert(obj);
    }
}
