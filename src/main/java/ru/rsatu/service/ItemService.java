package ru.rsatu.service;


import java.util.ArrayList;
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
import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.ItemsDetails;

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

    public int countItems() {
        Number ordersQTY = (Number) em.createQuery(" select count(Items) from Items ").getResultList().get(0);
        return ordersQTY.intValue();
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
            jo.put("name", item.getName());
            jo.put("qty", itemDetail.getQty());
            jo.put("child", rekursStructure(item));
            ja.add(jo);
        }
        return ja;
    }

    @Transactional
    public JsonObject getStructure (Items parentItem){
        JsonObject json = new JsonObject();
        json.put("id", parentItem.id);
        json.put("name", parentItem.getName());
        json.put("child", rekursStructure(parentItem));
        return json;
    }
    
    
}