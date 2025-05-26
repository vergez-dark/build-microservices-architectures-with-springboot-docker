package com.jeffrey.POST_SERVICE.models;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.Temporal;


@MappedSuperclass
@Data
@AllArgsConstructor
public class BaseModel {
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdOn;

    @Column(name = "last_update_on")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date lastUpdateOn;

    @Column(name = "deleted_at")
    @LastModifiedDate
    protected LocalDateTime deletedAt;

    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    protected short status;

    public BaseModel() {
        super();
        this.createdOn = new Date();
        this.lastUpdateOn = new Date();
    }
    
}
