package ru.rsatu.resources;

import io.vertx.core.json.JsonObject;
import ru.rsatu.pojo.Suppliers;
import ru.rsatu.service.SuppliersService;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/suppliers")
public class SuppliersResources {
    @Inject
    SuppliersService ss;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSuppliers")
    public Response getSuppliers(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = ss.countSuppliers();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 10.0));
        json.put("data", ss.getSuppliers(page));
        return Response.ok(json).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllSuppliers")
    public Response getAllSuppliers(){
        return Response.ok(ss.getAllSuppliers()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSupplierById")
    public Response getSupplierById(@QueryParam("id") Long id){
        return Response.ok(ss.getSupplierById(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/insertSupplier")
    public Response insertSupplier(Suppliers s){
        return Response.ok(ss.insertSupplier(s)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateSupplier")
    public Response updateSupplier(Suppliers s){
        return Response.ok(ss.updateSupplier(s)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteSupplier")
    public Response deleteSupplier(@QueryParam("id") Long id){
        ss.deleteSupplier(ss.getSupplierById(id));
        return Response.ok().build();
    }
}
