package ru.rsatu.pojo;

import javax.persistence.*;

@Entity
@Table(name="status")
public class Status {
    @Id
    @SequenceGenerator(name = "statusSeq", sequenceName = "status_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "statusSeq")
    private Integer id;
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

    public Integer getId() {
        return id;
    }
}
