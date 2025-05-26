package com.jeffrey.POST_SERVICE.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MediaRequest {
    // media_url .. file  (image or video)
    private String media_url;
}