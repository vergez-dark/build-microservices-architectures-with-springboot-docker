package com.blog.authentications.service;

import com.blog.authentications.dtos.ResponseDTO;
import com.blog.authentications.dtos.UserDto;
import com.blog.authentications.entities.AppRole;
import com.blog.authentications.entities.Token;
import com.blog.authentications.entities.Users;
import com.blog.authentications.exceptions.GeneralException;
import com.blog.authentications.exceptions.RessourceNotFoundException;
import com.blog.authentications.request.SignUpRequest;
import com.blog.authentications.response.SignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AccountService {

    Users addNewUser(Users user);
    UserDto getUserById(Long id) throws RessourceNotFoundException;
    AppRole addNewRole(AppRole role);
    void addRoleToUser(String username, String roleName);
    Users loadUserByUsername(String username);
    List<Users> listUsers();
    SignUpResponse signUp(SignUpRequest signUpRequest);
    ResponseDTO generateNewActivationHash(String holdHash) throws GeneralException;
    ResponseDTO activateAccount(String activationHash) throws GeneralException;
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception;
    Token findByTk(String tk);
}
