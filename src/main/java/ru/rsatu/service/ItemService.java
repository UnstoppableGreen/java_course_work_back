package ru.rsatu.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import ru.rsatu.pojo.Clients;
import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.ItemsDetails;
import ru.rsatu.pojo.Orders;
import ru.rsatu.pojo.OrdersDetails;

@ApplicationScoped
public class ItemService {
    @Inject
    EntityManager em;
	private List<Items> atomicItems = new ArrayList<>();

    public List<Items> getItems(int page) {
        Query query = em.createQuery(" select c from Items c ");
        query.setFirstResult((page - 1) * 10);
        query.setMaxResults(10);
        List<Items> listItems = query.getResultList();
        return listItems;
    }
    public List<Items> getItems() {
        return em.createQuery(" select c from Items c ", Items.class).getResultList();
    }

    public int countItems() {
        Number QTY = (Number) em.createQuery(" select count(id) from Items ").getResultList().get(0);
        return QTY.intValue();
    }

    //вставка данных
    @Transactional
    public Items insertItem(Items item) {
        em.merge(item);
        em.flush();
        em.clear();
        return item;
    }

    public Items getItemById(Long itemID) {
        Items item = em.find(Items.class, itemID);
        return item;
    }

    @Transactional
    public ItemsDetails createStructure(ItemsDetails details) {
        em.merge(details);
        em.flush();
        em.clear();
        return details;
    }
    //обновление данных
    @Transactional
    public Items updateItem(Items item) {
    	Date dateNow = new Date();
        em.merge(item);
        em.flush();
        return item;
    }
    //обновление данных
    @Transactional
    public ItemsDetails updateDetail(ItemsDetails detail) {
        em.merge(detail);
        em.flush();
        return detail;
    }
    public Map<Items, Integer> getAtomicsFromItem(Items parentItem,int savedQTY) {
        atomicItems.clear();
        getAllAtomicItems(parentItem, savedQTY);
        Map<Items, Integer> countMap = new HashMap<>();
        for (Items item: atomicItems) {

            if (countMap.containsKey(item))
                countMap.put(item, countMap.get(item) + 1);
            else
                countMap.put(item, 1);
        }
        return countMap;
    }
    @Transactional
    public List<Items> getAllAtomicItems (Items parentItem, int savedQTY){
    	System.out.println("Проверяемый родитель:"+parentItem.getName());
    	if (!parentItem.itemDetails.isEmpty()) { 
	    	List<ItemsDetails> childItemDetails = new ArrayList<>();    	
		    	for (ItemsDetails itemDetail : parentItem.itemDetails) {
		    		System.out.println("Проверяем потомков:"+itemDetail.toString());
		    		System.out.println("ID потомка: "+itemDetail.getItemID());
		    		Items item = em.find(Items.class, itemDetail.getItemID());
		    		int qty=itemDetail.getQty();
		    		System.out.println("Проверяемый потомок из ITEMS:"+item.toString());
		    		if (item.itemDetails.isEmpty()) {		    			
		    			//atomicItems.add(parentItem);
		    			for(int i = 1; i <= qty*savedQTY; i++) {
		    				System.out.println("Добавляем атомарного в список::"+item.toString());
		    				atomicItems.add(item);
		    				}
		    		}	    		
		    		else {
			    		Query query = em.createQuery(" select c from ItemsDetails c where itemID="+item.id);
			    		childItemDetails = query.getResultList();
			    		List<Items> childItems = new ArrayList<>();			    		
				    		for (ItemsDetails childItem  : childItemDetails) {
				    			System.out.println("Иначе смотрим его детей:"+childItem.toString()+", сохранённое количество: " +childItem.getQty());
				    			getAllAtomicItems(em.find(Items.class, childItem.getItemID()),childItem.getQty());		    			
				    		}		    	 
		    		}
		    	} 
    	}
    	else { 
			for(int i = 1; i <= savedQTY; i++) {
				System.out.println("Добавляем атомарного в список::"+parentItem.toString());
				atomicItems.add(parentItem);				
				}
			}
    return atomicItems;
    }
 

    private JsonArray rekursStructure(Items parentItem){
        JsonArray ja = new JsonArray();
        for (ItemsDetails itemDetail : parentItem.itemDetails) {
            Items item = em.find(Items.class, itemDetail.getItemID());
            JsonObject jo = new JsonObject();
            jo.put("id", item.id);
            jo.put("text"," ID: "+item.id+" "+item.getName()+ " QTY: "+itemDetail.getQty());
            jo.put("state",  JsonArrayState());
            jo.put("qty", itemDetail.getQty());
            jo.put("nodes", rekursStructure(item));
            ja.add(jo);
        }
        return ja;
    }

    private JsonObject JsonArrayState(){
        JsonObject jo = new JsonObject();
        jo.put("checked", false);
        jo.put("selected", false);
        jo.put("expanded", false);
        return jo;
    }

    @Transactional
    public JsonObject getStructure (Items parentItem,Integer qty){
        JsonObject json = new JsonObject();
        json.put("id", parentItem.id);
        json.put("text"," ID: "+parentItem.id+" "+ parentItem.getName()+" QTY: "+ qty);
        json.put("state",  JsonArrayState());
        json.put("nodes", rekursStructure(parentItem));
        return json;
    }
    
    
}