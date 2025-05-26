package com.jeffrey.POST_SERVICE.mappers;

import org.mapstruct.Mapper;
import com.jeffrey.POST_SERVICE.models.User;

import com.jeffrey.POST_SERVICE.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse (User user);
    
}
