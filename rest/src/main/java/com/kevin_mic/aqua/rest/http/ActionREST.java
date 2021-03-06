package com.kevin_mic.aqua.rest.http;

import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.action.ActionService;

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

@Path("/v1/actions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActionREST {
    private final ActionService actionService;

    @Inject
    public ActionREST(ActionService actionService) {
        this.actionService = actionService;
    }

    @GET
    public List<ActionInterface> listDevices() {
        return actionService.listActions();
    }

    @POST
    public ActionInterface createAction(ActionInterface action) {
        return actionService.addAction(action);
    }

    @GET
    @Path("/{actionId}")
    public ActionInterface getAction(@PathParam("actionId") int actionId) {
        return actionService.getAction(actionId);
    }

    @PUT
    @Path("/{actionId}")
    public ActionInterface updateAction(@PathParam("actionId") int actionId, ActionInterface update) {
        if (actionId != update.getActionId()) {
            throw new AquaException(ErrorType.IdMismatch);
        }
        return actionService.updateAction(actionId, update);
    }

    @DELETE
    @Path("/{actionId}")
    public void deleteAction(@PathParam("actionId") int actionId) {
        actionService.deleteAction(actionId);
    }
}
