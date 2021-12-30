package ru.rsatu.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
@Entity
public class Items extends PanacheEntity {
	private String name;
	private Long defaultSupplierID;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@OnDelete(action = OnDeleteAction.NO_ACTION)
	//@JsonIgnore
	public Suppliers suppliers;
	
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    public List<ItemsDetails> itemDetails;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDefaultSupplierID() {
		return defaultSupplierID;
	}

	public void setDefaultSupplierID(Long defaultSupplierID) {
		this.defaultSupplierID = defaultSupplierID;
	}
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", defaultSupplierID=" + defaultSupplierID +
                ", suppliers=" + suppliers +
                ", ITEMDETAILS=" + itemDetails +
                '}';
    }
    
}
