package ru.rsatu.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.Orders;
import ru.rsatu.pojo.OrdersDetails;
import ru.rsatu.pojo.Requests;
import ru.rsatu.pojo.Shipments;
@ApplicationScoped
public class ShipmentsAndDeliveryService {
    @Inject
    EntityManager em;
    
     
    @Inject
    ItemService is;
    
    @Transactional
    public Shipments insertShipment(Shipments shipment) {
        em.merge(shipment);
        em.flush();
        em.clear();
        return shipment;
    }
    
    @Transactional
    public Shipments updateShipment(Shipments shipment) {
        em.merge(shipment);
        em.flush();
        em.clear();
        return shipment;
    }
    
    public Shipments getShipmentById(Long shipmentID) {
    	Shipments shipment = em.find(Shipments.class, shipmentID);
        return shipment;
    }
    
    public List<Shipments> getShipments(int page) {
        Query query = em.createQuery(" select c from Shipments c ");
        query.setFirstResult((page - 1) * 10);
        query.setMaxResults(10);
        List<Shipments> listShipments = query.getResultList();
        return listShipments;
    }
    
    public int countShipments() {
        Number shipmentsQTY = (Number) em.createQuery(" select count(id) from Shipments ").getResultList().get(0);
        return shipmentsQTY.intValue() ;
    }
    
    
       
    @Transactional
    public Requests insertRequest(Requests request) {
    	Date dateNow = new Date();
    	request.setCreationDate(dateNow);  	
        em.merge(request);
        em.flush();
        em.clear();
    return request;
    }
    
    @Transactional
    public void createRequests(Orders order) { 
    	Date dateNow = new Date();
    	Requests request = new Requests();
    	for (OrdersDetails detail : order.orderDetails) { 
    		request.setOrderID(order.id);
    		request.setItemID(detail.getItemID());
    		request.setQty(detail.getQty());	
            request.setActualSupplierID(is.getItemById(detail.getItemID()).getDefaultSupplierID());
            request.setCreationDate(dateNow);
            request.setStatusID(15); //НЕИЗВЕСТНЫЙ СТАТУС ДЛЯ "РЕКВЕСТ СОЗДАН"
            em.merge(request);

    	}
    	em.flush();
        em.clear();   	
    }
    
	public Requests updateRequest(Requests request) {
        em.merge(request);
        em.flush();
        em.clear();
        return request;		
	}
    public Requests getRequestById(Long requestID) {
    	Requests request = em.find(Requests.class, requestID);
        return request;
    }
    
    public List<Requests> getRequests(int page) {
        Query query = em.createQuery(" select c from Requests c ");
        query.setFirstResult((page - 1) * 10);
        query.setMaxResults(10);
        List<Requests> listRequests = query.getResultList();
        return listRequests;
    }
    
    public int countRequest() {
        Number requestsQTY = (Number) em.createQuery(" select count(id) from Requests ").getResultList().get(0);
        return requestsQTY.intValue() ;
    }
	
	
}
