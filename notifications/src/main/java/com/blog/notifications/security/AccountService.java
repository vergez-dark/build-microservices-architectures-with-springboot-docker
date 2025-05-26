package com.blog.notifications.security;

import com.blog.notifications.entities.Users;

public interface AccountService {

    Users loadUserByUsername(String username);
}
