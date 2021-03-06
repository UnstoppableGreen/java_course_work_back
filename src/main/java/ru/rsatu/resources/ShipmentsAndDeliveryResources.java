package ru.rsatu.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import io.quarkus.security.Authenticated;
import io.vertx.core.json.JsonObject;
import ru.rsatu.pojo.Carriers;
import ru.rsatu.pojo.Requests;
import ru.rsatu.pojo.Shipments;
import ru.rsatu.service.ItemService;
import ru.rsatu.service.OrderService;
import ru.rsatu.service.ShipmentsAndDeliveryService;

@Authenticated

@Path("/ShipmentsAndDeliveries")
public class ShipmentsAndDeliveryResources {
    @Inject
    ShipmentsAndDeliveryService sads;
    @Inject
    ItemService is;
    @Inject
    OrderService os;
    
    @RolesAllowed({"editShipments"})
    @PUT
    @Path("/newShipment")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void newShipment(Shipments shipment) {
	    System.out.println("Попытка добавить отгрузку: \n"+shipment.toString());   	    	  
	    sads.insertShipment(shipment);
    }
    @RolesAllowed({"editShipments"})
    @PUT
    @Path("/updateShipment")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateShipment(Shipments shipment) { 
    	System.out.println("Попытка обновить отгрузку: \n"+shipment.toString());
    	sads.updateShipment(shipment);
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Path("/getShipmentDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShipmentDetails(@QueryParam("shipmentID") Long shipmentID) {   	
    	Shipments shipment = new Shipments();
    	shipment=sads.getShipmentById(shipmentID);
    	JsonObject json = new JsonObject();
    	json.put("shipmentID", shipment.id);
    	json.put("orderID", shipment.getOrderID());
    	json.put("clientID", os.getOrderById(shipment.getOrderID()).getClientID());
    	json.put("shipmentDate", shipment.getShipmentDate());
    	json.put("estimateDeliveryDate", shipment.getEstimateDeliveryDate());
    	json.put("deliveryDate", shipment.getDeliveryDate());
    	json.put("orderDetails", os.getOrderDetails(sads.getShipmentById(shipmentID).getOrderID()));
    	return Response.ok(json).build();
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Path("/getShipments")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response getShipments(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = sads.countShipments();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 4.0));
        json.put("data", sads.getShipments(page));
        return Response.ok(json).build();
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getShipmentById")
    public Response getShipmentById(@QueryParam("shipmentID") Long shimpentID){
        return Response.ok(sads.getShipmentById(shimpentID)).build();
    }
    @RolesAllowed({"editShipments"})
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteShipment")
    public Response deleteShipment(@QueryParam("shipmentID") Long id){
        sads.deleteShipment(sads.getShipmentById(id));
        return Response.ok().build();
    }
    @RolesAllowed({"editRequests"})
    @PUT
    @Path("/newRequest")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void newRequest(Requests request) {
	    System.out.println("Попытка добавить запрос к поставщику: \n"+request.toString());   	    	  
	    sads.insertRequest(request);
    }
    @RolesAllowed({"editRequests"})
    @POST
    @Path("/createRequests")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void createRequests(Requests request) {   	
	    sads.createRequests(request);
    }
    @RolesAllowed({"editRequests"})
    @PUT
    @Path("/updateRequest")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateRequest(Requests request) { 
    	System.out.println("Попытка обновить отгрузку: \n"+request.toString());
    	sads.updateRequest(request);
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Path("/getRequests")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response getRequests(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = sads.countRequests();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 4.0));
        json.put("data", sads.getRequests(page));
        return Response.ok(json).build();
    }
    
    @RolesAllowed({"watchAll"})
    @GET
    @Path("/getRequestDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestDetails(@QueryParam("requestID") Long requestID) {   	
    	Requests request = new Requests();
    	request=sads.getRequestById(requestID);
    	JsonObject json = new JsonObject();
    	json.put("requestID", request.id);
    	json.put("orderID", request.getOrderID());
    	json.put("itemID", request.getItemID());
    	json.put("creationDate", request.getCreationDate());
    	json.put("estimateDeliveryDate", request.getEstimateDeliveryDate());
    	json.put("deliveryDate", request.getDeliveryDate());
	
    	json.put("itemDetails", is.getAtomicsFromItem(is.getItemById(request.getItemID()),request.getQty()));
    	
    	return Response.ok(json).build();
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Path("/getRequestById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestById(@QueryParam("requestID") Long requestID) {   	
    	return Response.ok(sads.getRequestById(requestID)).build();
    }
    @RolesAllowed({"editRequests"})
    @POST
    @Path("/deleteRequest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRequest(@QueryParam("requestID") Long requestID) {     	
    	sads.deleteRequest(requestID);
    	return Response.ok().build();
    }
    //-----------------------------------------------------------------------------------------------------------
    // CARRIERS
    //-----------------------------------------------------------------------------------------------------------
    @RolesAllowed({"watchAll"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getCarriers")
    public Response getCarriers(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = sads.countCarriers();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 10.0));
        json.put("data", sads.getCarriers(page));
        return Response.ok(json).build();
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllCarriers")
    public Response getAllCarriers(){
        return Response.ok(sads.getAllCarriers()).build();
    }
    @RolesAllowed({"watchAll"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getCarrierById")
    public Response getCarriersById(@QueryParam("carrierID") Long id){
        return Response.ok(sads.getCarriersById(id)).build();
    }
    @RolesAllowed({"editCarriers"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createCarrier")
    public Response insertSupplier(Carriers car){
        return Response.ok(sads.insertCarriers(car)).build();
    }
    @RolesAllowed({"editCarriers"})
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCarrier")
    public Response updateSupplier(Carriers carriers){
        return Response.ok(sads.updateCarriers(carriers)).build();
    }
    @RolesAllowed({"editCarriers"})
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteCarrier")
    public Response deleteCarriers(@QueryParam("carrierID") Long id){
        sads.deleteCarriers(sads.getCarriersById(id));
        return Response.ok().build();
    }
    
    
}
