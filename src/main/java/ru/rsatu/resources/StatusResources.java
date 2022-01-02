package ru.rsatu.resources;

import ru.rsatu.pojo.Status;
import ru.rsatu.service.StatusService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusResources {
    @Inject
    StatusService sr;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getStatuses")
    public Response getStatuses(){
        return Response.ok(sr.getStatuses()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getStatusById")
    public Response getStatusById(@QueryParam("id") Long id){
        return Response.ok(sr.getStatusById(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/insertStatus")
    public Response insertStatus(Status status){
        return Response.ok(sr.insertStatus(status)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateStatus")
    public Response updateStatus(Status status){
        return Response.ok(sr.updateStatus(status)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteStatus")
    public Response deleteStatus(Status status){
        sr.deleteStatus(status);
        return Response.ok().build();
    }
}
