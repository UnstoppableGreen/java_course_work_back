package ru.rsatu.pojo;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Status extends PanacheEntity {

    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
