package com.jeffrey.POST_SERVICE.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jeffrey.POST_SERVICE.dto.MediaResponse;
import com.jeffrey.POST_SERVICE.models.Media;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    //map list of media to list of media response
    List<MediaResponse> toMediaResponse(List<Media> mediaList);
    
}
