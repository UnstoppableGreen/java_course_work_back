package ru.rsatu.resources;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.ItemsDetails;
import ru.rsatu.pojo.Orders;
import ru.rsatu.pojo.OrdersDetails;
import ru.rsatu.service.ItemService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/items")
public class ItemResources {

    @Inject
    ItemService os;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getItems")
    public Response getItems(@QueryParam("page") int page){
        JsonObject json = new JsonObject();
        json.put("page", page);
        json.put("per_page", 10);
        int c = os.countItems();
        json.put("total", c);
        json.put("total_pages", (int)Math.ceil(c / 10.0));
        json.put("data", os.getItems(page));
        return Response.ok(json).build();
    }
    
    @PUT
    @Path("/newItem")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void newItem(Items item) {   	 
    	System.out.println("Попытка добавить итем: \n"+item.toString());
    	for (ItemsDetails detail : item.itemDetails) {                    
            System.out.println("Внутри цикла: \n"+detail.toString());
            detail.item = item; 
    	} 
    os.insertItem(item);
    }
    
    @GET
    @Path("/getAtomics")
    @Transactional
   // @Consumes(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)	
    public Map<Items, Integer> getAtomics(@QueryParam("itemID") Long id ) {
       // System.out.println("Попытка добавить деталь: \n"+item.toString()); 
        return os.getAtomicsFromItem(os.getItemById(id),1);
    	
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getStructure")
    public Response getStructure(@QueryParam("itemID") Long id){
        return Response.ok(os.getStructure(os.getItemById(id))).build();
    }
   

//    @PUT
//    @Path("/createStructure")
//    @Transactional
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void createStructure(ItemsDetails details) {
//        System.out.println("Попытка добавить структуру: \n"+details.toString());
//        /*Items parent = os.insertItem(details.parent);
//        details.parent = parent;*/
//        List<Items> ch = new ArrayList<>();
//        for (Items item : details.children) {
//            System.out.println("Внутри цикла: \n"+item.toString());
//            //Items child = os.insertItem(item);
//            //ch.add(child);
//        }
//        //details.children = ch;
//        System.out.println("Попытка добавить структуру2: \n"+details.toString());
//        os.createStructure(details);
//    }
}