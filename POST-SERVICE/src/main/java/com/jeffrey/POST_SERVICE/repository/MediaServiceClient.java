package com.jeffrey.POST_SERVICE.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.jeffrey.POST_SERVICE.models.Media;

@FeignClient(name = "MEDIA-SERVICE", url = "${gateway.url:http://localhost:8888}")
public interface MediaServiceClient {

    
    @GetMapping("/api/media/post/{postId}")
   ResponseEntity <List<Media>> getMediaByPostId(@PathVariable("postId") Long postId);
    
    
}
