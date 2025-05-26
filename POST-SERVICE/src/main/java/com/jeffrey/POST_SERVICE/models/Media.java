package com.jeffrey.POST_SERVICE.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Media extends BaseModel {
    private Long id;
    private String name;
    private String url_media;
    private Long post_id;
}
