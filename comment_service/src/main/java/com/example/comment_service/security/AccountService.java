package com.example.comment_service.security;

import com.example.comment_service.model.Users;

public interface AccountService {

    Users loadUserByUsername(String username);
}
