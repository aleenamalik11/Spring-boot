package com.hazelsoft.springsecurityjpa.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "audit_record")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audit_id")
    private Long id;

    @Column(name = "last_updated_at")
    private Date lastUpdatedAt;

    private String details;

    public Audit(){
        super();
    }
    public Audit(Date lastUpdatedAt, String details) {
        this.lastUpdatedAt = lastUpdatedAt;
        this.details = details;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
