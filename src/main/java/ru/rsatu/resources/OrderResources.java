package ru.rsatu.resources;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.Orders;
import ru.rsatu.pojo.OrdersDetails;
import ru.rsatu.service.ClientService;
import ru.rsatu.service.ItemService;
import ru.rsatu.service.OrderService;
import ru.rsatu.service.StatusService;
@Path("/orders")
public class OrderResources {
    @Inject
    OrderService os;
    
    @Inject
    ClientService cs;
    
    @Inject
    StatusService ss;
    
    @Inject
    ItemService is;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getOrders")
    public Response getOrders(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = os.countOrders();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 4.0));
        json.put("data", os.getOrders(page));
        return Response.ok(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getOrders")
    public Response getOrders(){
        return Response.ok(os.getOrders()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getOrderById")
    public Response getOrderById(@QueryParam("orderID") Long orderID){
        return Response.ok(os.getOrderById(orderID)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteOrder")
    public Response deleteOrder(@QueryParam("orderID") Long orderID){
        os.deleteOrder(orderID);
        return Response.ok().build();
    }
     
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getOrderInfo")
    public Response getOrderInfo(@QueryParam("orderID") Long orderID){
    	
    	JsonObject json = new JsonObject();
    	Orders order = new Orders();
    	
    	order=os.getOrderById(orderID);
    	System.out.println("Смотрим инфу по заказу: \n"+order.toString());
    	json.put("orderID", order.id);
    	System.out.println("айди "+order.id);
    	json.put("client",cs.getClientById(order.getClientID()).getName());
    	System.out.println("клиент "+cs.getClientById(order.getClientID()).getName());
    	json.put("status", ss.getStatusById(order.getStatusID()).getName());
    	System.out.println("status "+ss.getStatusById(order.getStatusID()).getName());
    	json.put("creationDate", order.getCreationDate());
    	json.put("lastUpdateOn", order.getLastUpdateOn());
    	System.out.println("Новый джейсон заполнен.");
    	 JsonArray itemsStructures = new JsonArray();
    	//JsonObject itemsStructures = new JsonObject();    	    	
    	for (OrdersDetails detail : order.orderDetails) {
    		System.out.println("Смотрим струкуру итема: \n"+is.getItemById(detail.getItemID()));
    		itemsStructures.add(is.getStructure (is.getItemById(detail.getItemID())));
    		
    	}
    	json.put("itemsStructures", itemsStructures);
        return Response.ok(json).build();
    }
    
    
    @PUT
    @Path("/newOrder")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void newOrder(Orders order, @QueryParam("createRequest") Boolean createRequests) {
    	System.out.println("Попытка добавить заказ: \n"+order.toString());
    	    	 
    	for (OrdersDetails detail : order.orderDetails) {                    
            System.out.println("Внутри цикла: \n"+detail.toString());
            detail.order = order; 
    	} 
    os.insertOrder(order,createRequests);
    }
   
    @PUT
    @Path("/addDetail/{orderID}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDetail(OrdersDetails detail, @PathParam Long orderID) {
        Orders order = os.getOrderById(orderID);
        if (order == null) {
            return;
        }
        OrdersDetails OrderDetail = new OrdersDetails();
        OrderDetail.setItemID(detail.getItemID());
        OrderDetail.setQty(detail.getQty());
        OrderDetail.setComments(detail.getComments());
        
        OrderDetail.order = order;        
        OrderDetail.persist(); //????
        
        order.orderDetails.add(OrderDetail);       
        order.persist(); //????
        
    }

    @DELETE
    @Path("/deleteDetail")
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void deleteDetail(@QueryParam("detailID") Long detailID) {
    	OrdersDetails detail = os.getDetailById(detailID);
    	System.out.println("Удаляемый детэйл: \n"+detail.toString());
        if (detail != null) {
        	detail.order.orderDetails.remove(detail);
        	detail.delete();
        }
    }

    
    @PUT
    @Path("/updateOrder")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOrder(Orders order) { 
    	System.out.println("Попытка обновить заказ: \n"+order.toString());
    	for (OrdersDetails detail : order.orderDetails) {
            System.out.println("Внутри цикла: \n"+detail.toString());
            detail.order = order;
            if (detail.id==0) {            	
            	addDetail(detail, order.id);
            } else {
        os.updateDetail(detail);  }               
    	} 
    	os.updateOrder(order);
    }
}
