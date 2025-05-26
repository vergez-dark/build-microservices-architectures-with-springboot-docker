package com.blog.authentications.dtos;

import com.blog.authentications.model.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EntityBaseDTO {
    protected Long id;
    protected Date createdOn;
    protected Date lastUpdateOn;
    protected short statut;

    public EntityBaseDTO() {
        super();
        this.createdOn = new Date();
    }

    public EntityBaseDTO(EntityBase entityBase) {
        this.id = entityBase.getId();
        this.createdOn = entityBase.getCreatedOn();
        this.lastUpdateOn = entityBase.getLastUpdateOn();
    }

    public EntityBaseDTO(Long id, Date lastUpdateOn) {
        this.id = id;
        this.lastUpdateOn = lastUpdateOn;
    }

    public EntityBaseDTO(Long id) {
        this.id = id;
    }
}
