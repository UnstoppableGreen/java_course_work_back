package ru.rsatu.pojo;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
@Entity
public class OrdersDetails extends PanacheEntity {

    private Long itemID;
    private Integer qty;
    private String comments;
    
    @ManyToOne
    @JsonIgnore
    public Orders order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdersDetails)) {
            return false;
        }

        OrdersDetails other = (OrdersDetails) o;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	public Long getItemID() {
		return itemID;
	}

	public void setItemID(Long itemID) {
		this.itemID = itemID;
	}
	public void setOrder(Orders order) {
		this.order = order;
	}
	public Orders getOrder() {
		return order;
	}
	
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
    @Override
    public String toString() {
        return "DETAIL{" +
                "itemID='" + itemID + '\'' +
                ", qty='" + qty + '\'' +
                ", comments=" + comments +
                '}';
    }
}
