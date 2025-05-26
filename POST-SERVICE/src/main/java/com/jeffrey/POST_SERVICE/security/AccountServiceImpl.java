package com.jeffrey.POST_SERVICE.security;

import com.jeffrey.POST_SERVICE.models.User;
import com.jeffrey.POST_SERVICE.repository.UserServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final UserServiceClient userServiceClient;

    public AccountServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userServiceClient.findByUsername(username);
    }

}
