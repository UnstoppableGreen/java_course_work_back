package ru.rsatu.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
@Entity
//@Table(name="orders")
public class Orders extends PanacheEntity {

    private Long clientID;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // order из файла OrdersDetails
    public List<OrdersDetails> orderDetails;
    private Integer statusID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date lastUpdateOn;

	public Integer getStatusID() {
		return statusID;
	}
	public void setStatusID(Integer i) {
		this.statusID = i;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date date) {
		this.creationDate = date;
	}
	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}
	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}
	
    public void addDetail(OrdersDetails detail) {
    	orderDetails.add(detail);
        //detail.setOrder(this);
        detail.order=this;
    }
    public void removeDetail(OrdersDetails detail) {
    	orderDetails.remove(detail);
        detail.order=null;
    }
    @Override
    public String toString() {
        return "Order{" +
                "clientID='" + clientID + '\'' +
                ", statusID='" + statusID + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdateOn=" + lastUpdateOn +
                ", DETAILS=" + orderDetails +
                '}';
    }
    
    
}
