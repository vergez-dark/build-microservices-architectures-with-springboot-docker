package com.blog.authentications.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotEmpty
    private String username;
    @NotEmpty
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    //utiliser pour definir que cet attributs de la classe ne sera accessible que en ecriture
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 6)
    private String password;
}
