package com.example.comment_service.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityBaseDTO {
    protected Long id;
    protected Date createdOn;
    protected Date lastUpdateOn;
    protected short status;

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
