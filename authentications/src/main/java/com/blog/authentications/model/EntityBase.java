package com.blog.authentications.model;

import com.blog.authentications.dtos.EntityBaseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Data
@AllArgsConstructor
public class EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    protected Long id;

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

    public EntityBase() {
        super();
        this.createdOn = new Date();
    }

    public EntityBase(Long id) {
        this.id = id;
    }

    public EntityBase(Long id, Date createdOn) {
        this.id = id;
        this.createdOn = createdOn;
    }

    public EntityBase(EntityBaseDTO entityBaseDTO) {
        this.id = entityBaseDTO.getId();
        this.createdOn = entityBaseDTO.getCreatedOn();
        this.lastUpdateOn = entityBaseDTO.getLastUpdateOn();
        this.status = entityBaseDTO.getStatut();
    }

    public static EntityBaseDTO fromEntityBase(EntityBase entity) {
        return new EntityBaseDTO(entity);
    }
}
