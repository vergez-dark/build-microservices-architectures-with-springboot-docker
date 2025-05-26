package com.jeffrey.POST_SERVICE.dto;

// import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private Long user_id;
    private Long category_id;
    // private List <MediaRequest> medias;
   
}
