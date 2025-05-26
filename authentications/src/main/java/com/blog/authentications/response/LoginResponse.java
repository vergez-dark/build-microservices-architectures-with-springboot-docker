package com.blog.authentications.response;

import com.blog.authentications.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Users user;
}
