package com.example.comment_service.security;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.comment_service.model.Users;
import com.example.comment_service.repository.UserServiceClient;

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
