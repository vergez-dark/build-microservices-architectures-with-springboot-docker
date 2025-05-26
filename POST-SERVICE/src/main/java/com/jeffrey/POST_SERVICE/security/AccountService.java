package com.jeffrey.POST_SERVICE.security;

import com.jeffrey.POST_SERVICE.models.User;

public interface AccountService {

    User loadUserByUsername(String username);
}
