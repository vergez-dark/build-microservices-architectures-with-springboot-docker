package com.jeffrey.POST_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse  {
    private Long id;
    private String name;
    private String url_media;
    private Long post_id;
}
