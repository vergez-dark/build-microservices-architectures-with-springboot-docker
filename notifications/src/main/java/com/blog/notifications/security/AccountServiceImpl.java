package com.blog.notifications.security;

import com.blog.notifications.client.UserServiceClient;
import com.blog.notifications.entities.Users;
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
    public Users loadUserByUsername(String username) {
        return userServiceClient.findByUsername(username);
    }

}
