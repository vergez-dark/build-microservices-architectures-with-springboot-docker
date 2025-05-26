package com.jeffrey.POST_SERVICE.dto;

import lombok.Data;
import java.util.Date;
import com.jeffrey.POST_SERVICE.models.BaseModel;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Data
public class BaseModelDTO {
    protected Date createdOn;
    protected Date lastUpdateOn;
    protected short status;

    public BaseModelDTO() {
        super();
        this.createdOn = new Date();
        this.lastUpdateOn = new Date();
    }

    public BaseModelDTO(BaseModel baseModel) {
        this.createdOn = baseModel.getCreatedOn();
        this.lastUpdateOn = baseModel.getLastUpdateOn();
    }

    public BaseModelDTO(Long id, Date lastUpdateOn) {
        this.lastUpdateOn = lastUpdateOn;
    }

    public BaseModelDTO(Long id) {
    }
}
